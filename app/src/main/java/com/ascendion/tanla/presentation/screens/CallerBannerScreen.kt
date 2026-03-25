package com.ascendion.tanla.presentation.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ascendion.tanla.ui.theme.TanlaPocTheme

@Composable
fun CallerBannerScreen(
    number: String,
    callerName: String = "Unknown Caller",
    isSpam: Boolean = false,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onReportSpam: () -> Unit
) {

    //  Background
    val cardBackground = if (isSpam) {
        Color(0xFFFF7043)
    } else {
        MaterialTheme.colorScheme.surface
    }

    // Text colors
    val titleColor = if (isSpam) {
        Color.White
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val subtitleColor = if (isSpam) {
        Color.White.copy(alpha = 0.8f)
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        contentAlignment = Alignment.TopCenter
    ) {

        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {

            Column(
                modifier = Modifier
                    .background(cardBackground)
                    .padding(16.dp)
            ) {

                // 🔹 Top Row WITH CLOSE BUTTON
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = callerName,
                            color = titleColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = number,
                            color = subtitleColor,
                            fontSize = 14.sp
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        //  Spam badge
                        if (isSpam) {
                            Box(
                                modifier = Modifier
                                    .background(Color.Red, RoundedCornerShape(50))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Spam",
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        //  CLOSE BUTTON
                        IconButton(
                            onClick = onReject
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = if (isSpam) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 🔹 Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    //  ACCEPT
                    Button(
                        onClick = onAccept,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2E7D32)
                        )
                    ) {
                        Text("Accept", color = Color.White)
                    }

                    //  DECLINE
                    Button(
                        onClick = onReject,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        )
                    ) {
                        Text("Decline", color = Color.White)
                    }

                    // ⚪ REPORT
                    OutlinedButton(
                        onClick = onReportSpam,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (isSpam) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text("Report")
                    }
                }
            }
        }
    }
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CallerBannerMultiPreview() {
    TanlaPocTheme {
        CallerBannerScreen(
            number = "+91 9876543210",
            callerName = "Spam Caller",
            isSpam = true,
            onAccept = {},
            onReject = {},
            onReportSpam = {}
        )
    }
}