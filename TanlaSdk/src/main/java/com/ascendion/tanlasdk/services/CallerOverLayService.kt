package com.ascendion.tanlasdk.services

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.ascendion.tanlasdk.ui.renderer.OverlayActions

import com.ascendion.tanlasdk.core.TanlaCallScreeningSDK
import com.ascendion.tanlasdk.core.TanlaSDKConfigHolder
import kotlinx.coroutines.*

@RequiresApi(Build.VERSION_CODES.S)
class CallerOverLayService : Service() {

    private lateinit var windowManager: WindowManager
    private var overlayView: View? = null

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private lateinit var telephonyManager: TelephonyManager
    private var callback: TelephonyCallback? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val number = intent?.getStringExtra("number") ?: "UNKNOWN"

        setupOverlay(number)
        startTimeout()
        registerCallStateListener()

        return START_NOT_STICKY
    }

    private fun setupOverlay(number: String) {

        // 🔥 Get renderer from SDK config
        val renderer = TanlaSDKConfigHolder.getRenderer()

        // 🔥 Create view using renderer (Compose OR XML)
        overlayView = renderer.createView(
            context = this,
            number = number,
            actions = object : OverlayActions {

                override fun onAccept() {
                    TanlaCallScreeningSDK.uiHandler.onAccept()
                    stopSelf()
                }

                override fun onDecline() {
                    TanlaCallScreeningSDK.uiHandler.onDecline()
                    stopSelf()
                }

                override fun onReport() {
                    TanlaCallScreeningSDK.uiHandler.onReport()
                    stopSelf()
                }
            }
        )

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP
        }

        windowManager.addView(overlayView, params)
    }

    private fun startTimeout() {
        scope.launch {
            delay(10_000)
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

        overlayView?.let {
            windowManager.removeView(it)
        }

        overlayView = null
    }

    override fun onBind(intent: Intent?): IBinder? = null
}