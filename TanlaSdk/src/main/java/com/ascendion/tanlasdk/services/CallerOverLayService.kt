package com.ascendion.tanlasdk.services

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.view.Gravity
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
class CallerOverLayService : Service() {
    private lateinit var windowsManager: WindowManager
    private var composeView: ComposeView? = null
    private val scope = CoroutineScope(context = SupervisorJob() + Dispatchers.Main)
    private lateinit var telephonyManager: TelephonyManager
    private var callback: TelephonyCallback? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val number = intent?.getStringExtra("number") ?: "UNKNOWN"
        setUpOverlay(number)
        startTimeOut()
        registerCallStateListener()
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        windowsManager = getSystemService(WINDOW_SERVICE) as WindowManager
    }

    private fun setUpOverlay(number: String) {
        composeView?.let {
            ComposeView(this).apply {
//                setContent {
//                    CallerBannerScreen(
//                        number,
//                        onAccept = { stopSelf() },
//                        onReject = { stopSelf() },
//                        onReportSpam = { stopSelf() }
//                    )
//                }
            }
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP
        }

        windowsManager.addView(composeView, params)
    }

    private fun startTimeOut() {
        scope.launch {
            delay(10000)
            stopSelf()
        }
    }

    private fun registerCallStateListener() {
        telephonyManager = getSystemService(TelephonyManager::class.java)

        callback = object : TelephonyCallback(), TelephonyCallback.CallStateListener {
            override fun onCallStateChanged(state: Int) {
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    stopSelf()
                }
            }
        }

        telephonyManager.registerTelephonyCallback(
            mainExecutor,
            callback as TelephonyCallback
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        callback?.let {
            telephonyManager.unregisterTelephonyCallback(it)
        }
        composeView?.let {
            windowsManager.removeView(composeView)
        }
        composeView = null
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}
