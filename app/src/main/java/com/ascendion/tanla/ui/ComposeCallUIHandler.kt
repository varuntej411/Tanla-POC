package com.ascendion.tanla.ui

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import com.ascendion.tanla.presentation.screens.CallerBannerScreen
import com.ascendion.tanlasdk.core.CallUIHandler

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.setViewTreeLifecycleOwner

import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner

class ComposeCallUIHandler(
    private val context: Context
) : CallUIHandler {

    //Provides Lifecycle + SavedState support for Compose inside overlay
    private class OverlayLifecycleOwner :
        LifecycleOwner,
        SavedStateRegistryOwner {

        private val lifecycleRegistry = LifecycleRegistry(this)
        private val savedStateRegistryController =
            SavedStateRegistryController.create(this)

        init {
            savedStateRegistryController.performAttach()
            savedStateRegistryController.performRestore(null)
            lifecycleRegistry.currentState = Lifecycle.State.RESUMED
        }

        override val lifecycle: Lifecycle
            get() = lifecycleRegistry

        override val savedStateRegistry: SavedStateRegistry
            get() = savedStateRegistryController.savedStateRegistry
    }

    private var windowManager: WindowManager? = null
    private var overlayView: View? = null

    // Shows overlay UI for incoming call
    override fun showIncomingCallUI(number: String) {

        val appContext = context.applicationContext

        windowManager =
            appContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val owner = OverlayLifecycleOwner()
        val composeView = ComposeView(appContext).apply {

            // Attach lifecycle + saved state (required for Compose in overlay)
            setViewTreeLifecycleOwner(owner)
            setViewTreeSavedStateRegistryOwner(owner)

            setContent {
                CallerBannerScreen(
                    number = number,
                    callerName = "Test Caller",
                    isSpam = true,
                    onAccept = { onAccept() },
                    onReject = { onDecline() },
                    onReportSpam = { onReport() }
                )
            }
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP
        }

        overlayView = composeView

        try {
            windowManager?.addView(overlayView, params)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Removes overlay UI
    override fun removeUI() {
        try {
            overlayView?.let {
                windowManager?.removeView(it)
                overlayView = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onAccept() {
        removeUI()
    }


    override fun onDecline() {
        removeUI()
    }


    override fun onReport() {
        // TODO: API integration
    }
}