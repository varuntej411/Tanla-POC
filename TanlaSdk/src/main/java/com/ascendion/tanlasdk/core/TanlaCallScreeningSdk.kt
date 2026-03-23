package com.ascendion.tanlasdk.core

import android.app.Activity
import android.app.Application
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
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

    // 🔥 Central UI handler (used by services)
    lateinit var uiHandler: CallUIHandler
        private set

    /**
     * SDK Initialization
     */
    fun initialize(application: Application, config: TanlaSDKConfig) {

        if (isInitialized) return

        appContext = application.applicationContext

        // 🔥 Store config safely
        TanlaSDKConfigHolder.initialize(config)

        // 🔥 Setup logger
        Logger.enabled = config.enableLogging

        // 🔥 Initialize UI handler
        uiHandler = DefaultCallUIHandler(appContext)

        isInitialized = true
    }

    /**
     * Ensures SDK is initialized before usage
     */
    fun checkInit() {
        check(isInitialized) {
            "TanlaCallScreeningSDK is not initialized. Call initialize() first."
        }
    }

    /**
     * Request default dialer role (required for call handling)
     */
    fun requestDefaultDialer(activity: Activity) {

        val telecomManager = activity.getSystemService(TelecomManager::class.java)

        if (telecomManager == null) {
            Toast.makeText(activity, "Telecom service not available", Toast.LENGTH_SHORT).show()
            Logger.log("TelecomManager is null")
            return
        }

        // Already default
        if (telecomManager.defaultDialerPackage == activity.packageName) {
            Toast.makeText(activity, "App is already default dialer", Toast.LENGTH_SHORT).show()
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = activity.getSystemService(RoleManager::class.java)

            if (roleManager != null &&
                roleManager.isRoleAvailable(RoleManager.ROLE_DIALER) &&
                !roleManager.isRoleHeld(RoleManager.ROLE_DIALER)
            ) {
                val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
                activity.startActivityForResult(intent, REQUEST_DEFAULT_DIALER)
                return
            }
        }

        // Fallback (Android 6–9)
        val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
        intent.putExtra(
            TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME,
            activity.packageName
        )

        try {
            activity.startActivityForResult(intent, REQUEST_DEFAULT_DIALER)
        } catch (e: Exception) {
            Toast.makeText(activity, "Unable to request default dialer", Toast.LENGTH_SHORT).show()
            Logger.log("Default dialer intent failed: ${e.message}")
        }
    }

    /**
     * Request overlay permission (needed for floating UI)
     */
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