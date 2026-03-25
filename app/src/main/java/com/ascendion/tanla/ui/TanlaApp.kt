package com.ascendion.tanla.ui

import android.app.Application
import com.ascendion.tanlasdk.core.TanlaCallScreeningSDK
import com.ascendion.tanlasdk.core.TanlaSDKConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TanlaApp: Application() {
    override fun onCreate() {
        super.onCreate()
        TanlaCallScreeningSDK.initialize(
            this,
            TanlaSDKConfig(
                baseUrl = "https://api.example.com/",
                apiKey = "123456",
                enableLogging = true,
                enableOverlay = true
            )
        )
        TanlaCallScreeningSDK.uiHandler = ComposeCallUIHandler(this)
    }
}