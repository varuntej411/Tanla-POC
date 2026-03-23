package com.ascendion.tanlasdk.core

import android.content.Context
import android.content.Intent
import com.ascendion.tanlasdk.services.CallerOverLayService

class DefaultCallUIHandler(
    private val context: Context
) : CallUIHandler {

    override fun showIncomingCallUI(number: String) {
        val intent = Intent(context, CallerOverLayService::class.java)
        intent.putExtra("number", number)
        context.startService(intent)
    }

    override fun removeUI() {
        context.stopService(Intent(context, CallerOverLayService::class.java))
    }

    override fun onAccept() {
        removeUI()
    }

    override fun onDecline() {
        removeUI()
    }

    override fun onReport() {
        removeUI()
    }
}