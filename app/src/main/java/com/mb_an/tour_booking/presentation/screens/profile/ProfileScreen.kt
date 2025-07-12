package com.mb_an.tour_booking.presentation.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ProfileScreen(
    isLoggedIn: Boolean,
    email:      String,
    onLogin:    () -> Unit,
    onLogout:   () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Tài khoản",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                actions = {
                    if (isLoggedIn) {
                        TextButton(
                            onClick = onLogout,
                            colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                        ) {
                            Text("Đăng xuất")
                        }
                    } else {
                        TextButton(onClick = onLogin) {
                            Text("Đăng nhập")
                        }
                    }
                },
                colors = centerAlignedTopAppBarColors(
                    containerColor     = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor  = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement   = Arrangement.Center,
            horizontalAlignment   = Alignment.CenterHorizontally
        ) {
            if (isLoggedIn) {
                Text(
                    text = "Email của bạn:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                )
            } else {
                Text(
                    text = "Bạn chưa đăng nhập",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}