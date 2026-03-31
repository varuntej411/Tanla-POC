package com.ascendion.tanlasdk.core

import android.app.Activity
import android.app.Application
import android.app.role.RoleManager
import android.content.Context
import android.content.Context.ROLE_SERVICE
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.telecom.TelecomManager
import android.widget.Toast
import androidx.core.net.toUri

object TanlaCallScreeningSdk {

    private lateinit var appContext: Context
    const val REQUEST_DEFAULT_DIALER = 1

    private var isInitialized = false
    var uiHandler: CallUIHandler? = null

    fun initialize(application: Application, tanlaSdkConfig: TanlaSDKConfig) {
        if (isInitialized) return

        appContext = application.applicationContext
        TanlaSDKConfigHolder.tanlaSdkConfig = tanlaSdkConfig

        Logger.enabled = tanlaSdkConfig.enableLogging
        Logger.log("SDK initialized")

        isInitialized = true
    }

    fun checkInit() {
        check(value = isInitialized) {
            "CallScreeningSDK not initialized"
        }
    }

    /**
     * Requests the user to set this app as the default call screening app.
     */
    fun requestDefaultDialer(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = activity.getSystemService(RoleManager::class.java)
            if (roleManager != null && roleManager.isRoleAvailable(RoleManager.ROLE_CALL_SCREENING)) {
                if (!roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)) {
                    val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
                    activity.startActivityForResult(intent, REQUEST_DEFAULT_DIALER)
                } else {
                    Toast.makeText(activity, "App already holds Call Screening role", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Fallback for older versions if needed, though ROLE_CALL_SCREENING is Q+
            Toast.makeText(activity, "Call screening not supported on this version", Toast.LENGTH_SHORT).show()
        }
    }


    fun requestOverlayPermission(activity: Activity) {
        if (!Settings.canDrawOverlays(activity)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                "package:${appContext.packageName}".toUri()
            )
            activity.startActivity(intent)
        }
    }

}
