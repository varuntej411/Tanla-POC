package com.ascendion.tanlasdk.services

import android.content.Intent
import android.telecom.Call
import android.telecom.CallScreeningService
import com.ascendion.tanlasdk.core.Logger
import com.ascendion.tanlasdk.core.TanlaCallScreeningSdk

class TanlaCallScreeningService: CallScreeningService() {

    override fun onScreenCall(callDetails: Call.Details) {
        Logger.log("onScreenCall triggered for: ${callDetails.handle}")
        
        try {
            TanlaCallScreeningSdk.checkInit()
        } catch (e: Exception) {
            Logger.log("SDK not initialized: ${e.message}")
            return
        }

        // Extract number safely
        val number = callDetails.handle?.schemeSpecificPart ?: "UNKNOWN"

        // Start Overlay Service
        val intent = Intent(this, CallerOverLayService::class.java).apply {
            putExtra("number", number)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        
        try {
            // Note: On Android 12+, background service start might have restrictions.
            // But CallScreeningService is usually allowed to start services for UI.
            startService(intent)
        } catch (e: Exception) {
            Logger.log("Failed to start CallerOverLayService: ${e.message}")
        }

        val response = CallResponse.Builder()
            .setDisallowCall(false)
            .setRejectCall(false)
            .setSkipCallLog(false)
            .setSkipNotification(false)
            .build()

        respondToCall(callDetails, response)
    }
}
