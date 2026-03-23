package com.ascendion.tanlasdk.ui.renderer

import android.content.Context
import android.view.View

interface OverlayRenderer {

    fun createView(
        context: Context,
        number: String,
        actions: OverlayActions
    ): View
}