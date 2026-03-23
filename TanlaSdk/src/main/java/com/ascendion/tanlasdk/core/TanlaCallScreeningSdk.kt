package com.ascendion.tanlasdk.core

import android.app.Activity
import android.app.Application
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.telecom.TelecomManager
import android.widget.Toast
import androidx.core.net.toUri

object TanlaCallScreeningSDK {

    private lateinit var appContext: Context
    const val OVERLAY_PERMISSION_REQUEST_CODE = 1000
    const val REQUEST_DEFAULT_DIALER = 2000

    private var isInitialized = false
    var uiHandler: CallUIHandler? = null

    fun initialize(application: Application, tanlaSdkConfig: TanlaSDKConfig) {
        // To Avoid multi init
        if (isInitialized) return

        appContext = application.applicationContext
        TanlaSDKConfigHolder.tanlaSdkConfig = tanlaSdkConfig

        Logger.enabled = tanlaSdkConfig.enableLogging
      //  Logger.log("SDK initialized")

        isInitialized = true
    }

    fun checkInit() {
        check(value = isInitialized) {
            "CallScreeningSDK not initialized"
        }
    }

    /**
     * Requests the user to set this app as the default dialer.
     * Works for Android 6–14+ with ROLE_DIALER support for Android 10+.
     */
    fun requestDefaultDialer(activity: Activity) {
        val telecomManager = activity.getSystemService(TelecomManager::class.java)

        if (telecomManager == null) {
            Toast.makeText(activity, "Telecom service not available", Toast.LENGTH_SHORT).show()
            Logger.log("TelecomManager is null")
            return
        }

        // Already default?
        if (telecomManager.defaultDialerPackage == activity.packageName) {
            Toast.makeText(activity, "App is already default dialer", Toast.LENGTH_SHORT).show()
            return
        }

        // Modern Android 10+ using RoleManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = activity.getSystemService(RoleManager::class.java)
            if (roleManager != null && roleManager.isRoleAvailable(RoleManager.ROLE_DIALER)) {
                if (!roleManager.isRoleHeld(RoleManager.ROLE_DIALER)) {
                    val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
                    activity.startActivityForResult(intent, REQUEST_DEFAULT_DIALER)
                    return
                }
            }
        }

        // Fallback for older Android versions (6–9)
        val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
        intent.putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, activity.packageName)
        try {
            activity.startActivityForResult(intent, REQUEST_DEFAULT_DIALER)
        } catch (e: Exception) {
            Toast.makeText(activity, "Unable to request default dialer", Toast.LENGTH_SHORT).show()
            Logger.log("Failed to launch default dialer intent: ${e.message}")
        }
    }


    fun requestOverlayPermission(activity: Activity) {
        if (!Settings.canDrawOverlays(activity)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                "package:${activity.packageName}".toUri()
            )
            activity.startActivity(intent)
        }
    }

}