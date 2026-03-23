package com.ascendion.tanlasdk.core

data class TanlaSDKConfig(
    val baseUrl: String,
    val apiKey: String,
    val enableLogging: Boolean = false,
    val enableOverlay: Boolean = true
)