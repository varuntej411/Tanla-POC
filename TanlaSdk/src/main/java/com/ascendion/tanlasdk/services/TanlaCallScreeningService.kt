package com.ascendion.tanlasdk.services

import android.content.Intent
import android.telecom.Call
import android.telecom.CallScreeningService
import com.ascendion.tanlasdk.core.TanlaCallScreeningSDK
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TanlaCallScreeningService: CallScreeningService() {

    override fun onScreenCall(callDetails: Call.Details) {
        // Ensure to check this sdk initialized first
        TanlaCallScreeningSDK.checkInit()

        val number = callDetails.handle.schemeSpecificPart
        val response = CallResponse.Builder()
            .setDisallowCall(false)
            .setRejectCall(false)
            .setSkipCallLog(false)
            .setSkipNotification(false)
            .build()

        val intent = Intent(this, CallerOverLayService::class.java)
        intent.putExtra("number", number)
        startService(intent)

        respondToCall(callDetails, response)
    }
}