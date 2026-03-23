package com.ascendion.tanlasdk.core

import com.ascendion.tanlasdk.ui.renderer.ComposeOverlayRenderer
import com.ascendion.tanlasdk.ui.renderer.OverlayRenderer


object TanlaSDKConfigHolder {

    private var _config: TanlaSDKConfig? = null

    val config: TanlaSDKConfig
        get() = _config
            ?: throw IllegalStateException(
                "Tanla SDK is not initialized. Call TanlaCallScreeningSdk.initialize() first."
            )

    fun initialize(config: TanlaSDKConfig) {
        _config = config
    }

    /**
     * 🔥 Always returns a valid renderer
     * If host app didn't provide one → fallback to Compose
     */
    fun getRenderer(): OverlayRenderer {
        return config.overlayRenderer ?: ComposeOverlayRenderer()
    }

    /**
     * Optional helper
     */
    fun isInitialized(): Boolean = _config != null
}