package com.ascendion.tanlasdk.core

import com.ascendion.tanlasdk.ui.renderer.OverlayRenderer

data class TanlaSDKConfig(
    val baseUrl: String,
    val apiKey: String,
    val enableLogging: Boolean = false,
    val enableOverlay: Boolean = true,

    //  NEW: UI Renderer (Compose or XML)
    val overlayRenderer: OverlayRenderer? = null
)