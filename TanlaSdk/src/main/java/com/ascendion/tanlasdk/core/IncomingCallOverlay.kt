package com.ascendion.tanlasdk.core

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PhoneMissed
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IncomingCallOverlay(
    phoneNumber: String,
    onAnswerCall: () -> Unit,
    onRejectCall: () -> Unit,
    modifier: Modifier = Modifier
) {
   // val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF1A1A2E),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Incoming Call Text
            Text(
                text = "Incoming Call",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )

            // Phone Number Display
            Text(
                text = phoneNumber,
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            // Call Info
            Text(
                text = "Tap to answer or reject",
                color = Color.LightGray,
                fontSize = 14.sp
            )

            // Action Buttons Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reject Button
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            color = Color(0xFFE53935),
                            shape = CircleShape
                        )
                        .clickable {
                            onRejectCall()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.PhoneMissed,
                        contentDescription = "Reject Call",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Answer Button
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            color = Color(0xFF4CAF50),
                            shape = CircleShape
                        )
                        .clickable {
                            onAnswerCall()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Call,
                        contentDescription = "Answer Call",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun IncomingCallOverlayFullScreen(
    phoneNumber: String,
    onAnswerCall: () -> Unit,
    onRejectCall: () -> Unit
) {
   // val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D1B))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.CenterVertically)
        ) {
            // Caller Avatar Circle
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        color = Color(0xFF16213E),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = phoneNumber.take(1),
                    color = Color.White,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Caller Info
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Incoming Call",
                    color = Color(0xFFBBBBBB),
                    fontSize = 14.sp
                )

                Text(
                    text = phoneNumber,
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Mobile",
                    color = Color(0xFF888888),
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp),
                horizontalArrangement = Arrangement.spacedBy(30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reject Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(80.dp)
                        .background(
                            color = Color(0xFF8B0000),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable {
                            onRejectCall()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.PhoneMissed,
                            contentDescription = "Reject",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "Reject",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Answer Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(80.dp)
                        .background(
                            color = Color(0xFF00AA00),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable {
                            onAnswerCall()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Call,
                            contentDescription = "Answer",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "Answer",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewOverLay() {
    IncomingCallOverlayFullScreen("811117171", onAnswerCall = {}, onRejectCall = {})
}

