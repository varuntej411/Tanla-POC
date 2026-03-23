package com.ascendion.tanlasdk.core

import android.content.Context

interface CallUIHandler {
    fun onIncomingCall(context: Context, number: String)
}