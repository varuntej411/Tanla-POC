package com.ascendion.tanlasdk.services

import android.Manifest
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.telecom.TelecomManager
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.view.Gravity
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.ascendion.tanlasdk.core.IncomingCallOverlayFullScreen
import com.ascendion.tanlasdk.core.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
class CallerOverLayService : Service(), LifecycleOwner, ViewModelStoreOwner, SavedStateRegistryOwner {
    private lateinit var windowsManager: WindowManager
    private var composeView: ComposeView? = null
    private val scope = CoroutineScope(context = SupervisorJob() + Dispatchers.Main)
    private lateinit var telephonyManager: TelephonyManager
    private var callback: TelephonyCallback? = null
    private var callEnded = mutableStateOf(false)

    private val lifecycleRegistry = LifecycleRegistry(this)
    private val store = ViewModelStore()
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    override val lifecycle: Lifecycle get() = lifecycleRegistry
    override val viewModelStore: ViewModelStore get() = store
    override val savedStateRegistry: SavedStateRegistry get() = savedStateRegistryController.savedStateRegistry

    @RequiresPermission(Manifest.permission.ANSWER_PHONE_CALLS)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val number = intent?.getStringExtra("number") ?: "UNKNOWN"
        Logger.log("CallerOverLayService onStartCommand with number: $number")
        setUpOverlay(number)
        startTimeOut()
        registerCallStateListener()
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        Logger.log("CallerOverLayService onCreate")
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        windowsManager = getSystemService(WINDOW_SERVICE) as WindowManager
    }

    @RequiresPermission(Manifest.permission.ANSWER_PHONE_CALLS)
    private fun setUpOverlay(number: String) {
        if (composeView != null) {
            Logger.log("Overlay already exists, skipping creation")
            return
        }

        Logger.log("Creating overlay for $number")
        composeView = ComposeView(this).apply {
            setViewTreeLifecycleOwner(this@CallerOverLayService)
            setViewTreeViewModelStoreOwner(this@CallerOverLayService)
            setViewTreeSavedStateRegistryOwner(this@CallerOverLayService)
            setContent {
                IncomingCallOverlayFullScreen(
                    phoneNumber = number,
                    onAnswerCall = {
                        Logger.log("Answer button clicked")
                        answerCall()
                        stopSelf()
                    },
                    onRejectCall = {
                        Logger.log("Reject button clicked")
                        rejectCall()
                        stopSelf()
                    }
                )
            }
        }

        val layoutType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            layoutType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER
        }

        try {
            windowsManager.addView(composeView, params)
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            Logger.log("Overlay added to WindowManager")
        } catch (e: Exception) {
            Logger.log("Error adding overlay: ${e.message}")
            e.printStackTrace()
        }
    }

    @RequiresPermission(Manifest.permission.ANSWER_PHONE_CALLS)
    private fun answerCall() {
        try {
            val telecomManager = getSystemService(TelecomManager::class.java)
            telecomManager?.acceptRingingCall()
        } catch (e: Exception) {
            Logger.log("Error answering call: ${e.message}")
            e.printStackTrace()
        }
    }

    @RequiresPermission(Manifest.permission.ANSWER_PHONE_CALLS)
    private fun rejectCall() {
        try {
            val telecomManager = getSystemService(TelecomManager::class.java)
            telecomManager?.endCall()
        } catch (e: Exception) {
            Logger.log("Error rejecting call: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun startTimeOut() {
        scope.launch {
            delay(45000) // 45 seconds timeout
            Logger.log("Overlay timeout reached, stopping service")
            stopSelf()
        }
    }

    private fun registerCallStateListener() {
        telephonyManager = getSystemService(TelephonyManager::class.java)

        callback = object : TelephonyCallback(), TelephonyCallback.CallStateListener {
            override fun onCallStateChanged(state: Int) {
                Logger.log("Call state changed: $state")
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    callEnded.value = true
                    Logger.log("Call idle, stopping service")
                    stopSelf()
                }
            }
        }

        try {
            telephonyManager.registerTelephonyCallback(
                mainExecutor,
                callback as TelephonyCallback
            )
        } catch (e: Exception) {
            Logger.log("Error registering telephony callback: ${e.message}")
        }
    }

    override fun onDestroy() {
        Logger.log("CallerOverLayService onDestroy")
        super.onDestroy()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)

        scope.cancel()
        callback?.let {
            telephonyManager.unregisterTelephonyCallback(it)
        }
        composeView?.let {
            try {
                windowsManager.removeView(it)
                Logger.log("Overlay removed from WindowManager")
            } catch (e: Exception) {
                Logger.log("Error removing overlay: ${e.message}")
                e.printStackTrace()
            }
        }
        composeView = null
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}
