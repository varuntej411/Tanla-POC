package com.ascendion.tanlasdk.core

import android.util.Log

object Logger {
    var enabled = false

    fun log(msg: String) {
        if (enabled) Log.d("TanlaCallSDK", msg)
    }
}