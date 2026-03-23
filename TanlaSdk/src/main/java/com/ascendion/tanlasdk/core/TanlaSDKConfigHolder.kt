package com.ascendion.tanlasdk.core

object TanlaSDKConfigHolder {
    // this can use for inject anywhere we want to access the SDKConfig data class
    // like this way -> val baseUrl = SDKConfigHolder.config.baseUrl
    lateinit var tanlaSdkConfig: TanlaSDKConfig
}