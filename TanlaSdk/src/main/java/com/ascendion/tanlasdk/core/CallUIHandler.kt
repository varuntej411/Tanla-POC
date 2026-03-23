package com.ascendion.tanlasdk.core

interface CallUIHandler {

    /**
     * Called when incoming call detected
     */
    fun showIncomingCallUI(number: String)

    /**
     * Remove overlay / UI
     */
    fun removeUI()

    /**
     * User actions
     */
    fun onAccept()

    fun onDecline()

    fun onReport()
}