package com.ascendion.tanlasdk.ui.renderer

import android.content.Context
import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy


class ComposeOverlayRenderer : OverlayRenderer {

    override fun createView(
        context: Context,
        number: String,
        actions: OverlayActions
    ): View {

        return ComposeView(context).apply {

            // 🔥 VERY IMPORTANT (prevents memory leaks in overlays)
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnDetachedFromWindow
            )

//            setContent {
//                CallerBannerScreen(
//                    number = number,
//                    onAccept = { actions.onAccept() },
//                    onReject = { actions.onDecline() },
//                    onReportSpam = { actions.onReport() }
//                )
//            }
        }
    }
}