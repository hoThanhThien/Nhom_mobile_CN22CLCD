
package com.mb_an.tour_booking.presentation.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mb_an.tour_booking.presentation.viewmodel.AuthViewModel
import com.mb_an.tour_booking.presentation.viewmodel.AuthState
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = getViewModel()
) {
    val authState = viewModel.authState

    var email by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Quên mật khẩu") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.resetPassword(email.trim()) },
                enabled = authState !is AuthState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gửi email đặt lại mật khẩu")
            }
            TextButton(onClick = onNavigateToLogin) {
                Text("Quay lại Đăng nhập")
            }
            if (authState is AuthState.Error) {
                Text(authState.message, color = MaterialTheme.colorScheme.error)
            }
            if (authState is AuthState.PasswordResetSent) {
                Text("✅ Đã gửi email khôi phục mật khẩu")
            }
        }
    }

    LaunchedEffect(authState) {
        if (authState is AuthState.PasswordResetSent) {
            viewModel.resetState()
        }
    }
}
