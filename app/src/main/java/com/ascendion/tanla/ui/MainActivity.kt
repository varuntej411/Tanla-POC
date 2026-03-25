package com.ascendion.tanla.ui

import android.app.Activity
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ascendion.tanla.ui.theme.TanlaPocTheme
import com.ascendion.tanlasdk.core.TanlaCallScreeningSDK

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TanlaCallScreeningSDK.requestDefaultDialer(this)

        setContent {
            TanlaPocTheme {

                val handler = remember {
                    ComposeCallUIHandler(applicationContext)
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        handler = handler
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    handler: ComposeCallUIHandler
) {

    val context = LocalContext.current

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Button(
                onClick = {
                    if (Settings.canDrawOverlays(context)) {
                        handler.showIncomingCallUI("+91 9876543210")
                    } else {
                        val activity = context as? Activity
                        activity?.let {
                            TanlaCallScreeningSDK.requestOverlayPermission(it)
                        }
                    }
                }
            ) {
                Text("Show Overlay")
            }

            Button(
                onClick = {
                    handler.removeUI()
                }
            ) {
                Text("Remove Overlay")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    TanlaPocTheme {
        MainScreen(
            handler = ComposeCallUIHandler(
                context = android.app.Application()
            )
        )
    }
}