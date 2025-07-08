package com.mb_an.tour_booking.presentation.screens.auth

import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.mb_an.tour_booking.presentation.viewmodel.AuthViewModel
import com.mb_an.tour_booking.presentation.viewmodel.GoogleSignInViewModel
import com.mb_an.tour_booking.presentation.viewmodel.AuthState
import com.mb_an.tour_booking.presentation.viewmodel.GoogleSignInState
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgot: () -> Unit,
    authViewModel: AuthViewModel = getViewModel(),
    googleSignInViewModel: GoogleSignInViewModel = getViewModel()
) {
    val authState = authViewModel.authState
    val googleSignInState = googleSignInViewModel.googleSignInState

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    val activity = context as? Activity // ✅ Safe cast

    // Google Sign-In launcher
    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        if (task.isSuccessful) {
            val account = task.result
            account?.let {
                googleSignInViewModel.signInWithGoogle(it)
            }
        } else {
            googleSignInViewModel.resetState()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Đăng nhập") }) }
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
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mật khẩu") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    authViewModel.login(email.trim(), password)
                },
                enabled = authState !is AuthState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Đăng nhập")
            }
            Spacer(modifier = Modifier.height(8.dp))
            GoogleSignInButton(
                isLoading = googleSignInState is GoogleSignInState.Loading,
                onClick = {
                    activity?.let {
                        val googleClient = googleSignInViewModel.getGoogleSignInClient()
                        googleLauncher.launch(googleClient.signInIntent)
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onNavigateToRegister) {
                Text("Bạn chưa có tài khoản? Đăng ký")
            }
            if (authState is AuthState.Error) {
                Text(authState.message, color = MaterialTheme.colorScheme.error)
            }
            if (googleSignInState is GoogleSignInState.Error) {
                Text(googleSignInState.message, color = MaterialTheme.colorScheme.error)
            }
        }
    }

    // Navigate on success
    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            authViewModel.resetState()
            onLoginSuccess()
        }
    }
    LaunchedEffect(googleSignInState) {
        if (googleSignInState is GoogleSignInState.Success) {
            googleSignInViewModel.resetState()
            onLoginSuccess()
        }
    }
}
