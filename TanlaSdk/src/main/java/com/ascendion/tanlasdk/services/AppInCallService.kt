package com.ascendion.tanlasdk.services

import android.telecom.InCallService
import com.ascendion.tanlasdk.core.Logger

class AppInCallService : InCallService() {

    override fun onCallAdded(call: android.telecom.Call) {
        super.onCallAdded(call)
        Logger.log("Call added: ${call.details?.handle?.schemeSpecificPart}")
    }

    override fun onCallRemoved(call: android.telecom.Call) {
        super.onCallRemoved(call)
        Logger.log("Call removed: ${call.details?.handle?.schemeSpecificPart}")
    }

    override fun onBringToForeground(showDialpad: Boolean) {
        super.onBringToForeground(showDialpad)
        Logger.log("Bring to foreground called")
    }
}

