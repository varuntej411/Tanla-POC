package com.ascendion.tanlasdk.services

import android.telecom.Call
import android.telecom.CallScreeningService
import com.ascendion.tanlasdk.core.TanlaCallScreeningSDK

class TanlaCallScreeningService : CallScreeningService() {

    override fun onScreenCall(callDetails: Call.Details) {

        // 🔥 Ensure SDK initialized
        TanlaCallScreeningSDK.checkInit()

        val number = callDetails.handle.schemeSpecificPart ?: "UNKNOWN"

        // 🔥 Delegate UI handling to SDK
        TanlaCallScreeningSDK.uiHandler.showIncomingCallUI(number)

        // 🔥 Default response (POC)
        val response = CallResponse.Builder()
            .setDisallowCall(false)
            .setRejectCall(false)
            .setSkipCallLog(false)
            .setSkipNotification(false)
            .build()

        respondToCall(callDetails, response)
    }
}