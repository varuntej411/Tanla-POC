package com.ascendion.tanla.ui.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CallerBannerScreen(
    number: String,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onReportSpam: () -> Unit
) {

}

@Composable
@Preview
fun BannerView() {
    CallerBannerScreen(number = "", onAccept = {}, onReject = {}) {}
}