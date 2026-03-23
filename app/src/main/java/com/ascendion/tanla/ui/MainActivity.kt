package com.ascendion.tanla.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.ascendion.tanla.ui.theme.TanlaPocTheme
import com.ascendion.tanlasdk.core.TanlaCallScreeningSDK


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set XML or Compose handler
        // TanlaCallScreeningSDK.setUIHandler(XmlCallUIHandler())
        // Or Compose
        // CallScreeningSDK.setUIHandler(ComposeCallUIHandler())

         TanlaCallScreeningSDK.requestDefaultDialer(this)
        // TanlaCallScreeningSDK.requestOverlayPermission(this)

        setContent {
            TanlaPocTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {

        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    TanlaCallScreeningSDK.requestOverlayPermission(this@MainActivity)
                }
            ) {
                Text(
                    text = "Click me + $name",
                    color = Color.White
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        TanlaPocTheme {
            Greeting(
                "Android", modifier = Modifier
            )
        }
    }
}