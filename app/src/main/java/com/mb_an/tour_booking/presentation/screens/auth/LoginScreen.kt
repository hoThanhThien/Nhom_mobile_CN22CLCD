
package com.mb_an.tour_booking.presentation.screens.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.mb_an.tour_booking.R


@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit = {},
    onNavigateToForgot: () -> Unit = {}
) {
    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    // Context và Activity để khởi chạy Google Sign-In
    val context = LocalContext.current
    val activity = context as? Activity

    // 1) Tạo launcher cho Google Sign-In
    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.result
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            loading = true
            auth.signInWithCredential(credential)
                .addOnSuccessListener {
                    loading = false
                    onLoginSuccess()
                }
                .addOnFailureListener {
                    loading = false
                    errorMsg = it.localizedMessage
                }
        } catch (e: Exception) {
            loading = false
            errorMsg = e.localizedMessage
        }
    }

    // 2) Cấu hình GoogleSignInClient
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
    val googleClient = activity?.let { GoogleSignIn.getClient(it, gso) }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))

            Text(
                text = "Fast Travel",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))
            Text(
                text = "Create an account\nEnter your email to sign up for this app",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )

            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("email@domain.com") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Mật khẩu") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    loading = true
                    errorMsg = null
                    auth.signInWithEmailAndPassword(email.trim(), password)
                        .addOnSuccessListener {
                            loading = false
                            onLoginSuccess()
                        }
                        .addOnFailureListener {
                            loading = false
                            errorMsg = it.localizedMessage
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                enabled = !loading
            ) {
                Text("Continue")
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(modifier = Modifier.weight(1f))
                Text("  or  ", color = Color.Gray)
                Divider(modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(16.dp))

            // Google Sign-In button
            OutlinedButton(
                onClick = {
                    googleClient?.signInIntent?.let { intent ->
                        googleLauncher.launch(intent)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                border = ButtonDefaults.outlinedButtonBorder
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_google_logo),
                    contentDescription = "Google",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Continue with Google")
            }

            Spacer(Modifier.height(12.dp))

            // Apple Sign-In button (nếu bạn có)
            OutlinedButton(
                onClick = { /* TODO: Apple Sign-In */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                border = ButtonDefaults.outlinedButtonBorder
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_apple_logo),
                    contentDescription = "Apple",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Continue with Apple")
            }

            Spacer(Modifier.height(24.dp))

            TextButton(onClick = onNavigateToRegister) {
                Text("Chưa có tài khoản? Đăng ký")
            }
            TextButton(onClick = onNavigateToForgot) {
                Text("Quên mật khẩu?")
            }

            errorMsg?.let { msg ->
                Spacer(Modifier.height(12.dp))
                Text(msg, color = MaterialTheme.colorScheme.error)
            }
        }

        // overlay loading
        if (loading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color(0x88000000)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
