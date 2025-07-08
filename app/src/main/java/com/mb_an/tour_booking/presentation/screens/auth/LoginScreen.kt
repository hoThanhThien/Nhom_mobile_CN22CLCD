//package com.mb_an.tour_booking.presentation.screens.auth
//
//import android.app.Activity
//import androidx.compose.ui.platform.LocalContext
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.unit.dp
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.mb_an.tour_booking.presentation.viewmodel.AuthViewModel
//import com.mb_an.tour_booking.presentation.viewmodel.GoogleSignInViewModel
//import com.mb_an.tour_booking.presentation.viewmodel.AuthState
//import com.mb_an.tour_booking.presentation.viewmodel.GoogleSignInState
//import org.koin.androidx.compose.getViewModel
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LoginScreen(
//    onLoginSuccess: () -> Unit,
//    onNavigateToRegister: () -> Unit,
//    onNavigateToForgot: () -> Unit,
//    authViewModel: AuthViewModel = getViewModel(),
//    googleSignInViewModel: GoogleSignInViewModel = getViewModel()
//) {
//    val authState = authViewModel.authState
//    val googleSignInState = googleSignInViewModel.googleSignInState
//
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//
//    val context = LocalContext.current
//    val activity = context as? Activity // ✅ Safe cast
//
//    // Google Sign-In launcher
//    val googleLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//        if (task.isSuccessful) {
//            val account = task.result
//            account?.let {
//                googleSignInViewModel.signInWithGoogle(it)
//            }
//        } else {
//            googleSignInViewModel.resetState()
//        }
//    }
//
//    Scaffold(
//        topBar = { TopAppBar(title = { Text("Đăng nhập") }) }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .padding(padding)
//                .padding(16.dp)
//                .fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            OutlinedTextField(
//                value = email,
//                onValueChange = { email = it },
//                label = { Text("Email") },
//                modifier = Modifier.fillMaxWidth()
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            OutlinedTextField(
//                value = password,
//                onValueChange = { password = it },
//                label = { Text("Mật khẩu") },
//                visualTransformation = PasswordVisualTransformation(),
//                modifier = Modifier.fillMaxWidth()
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            Button(
//                onClick = {
//                    authViewModel.login(email.trim(), password)
//                },
//                enabled = authState !is AuthState.Loading,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Đăng nhập")
//            }
//            Spacer(modifier = Modifier.height(8.dp))
//            GoogleSignInButton(
//                isLoading = googleSignInState is GoogleSignInState.Loading,
//                onClick = {
//                    activity?.let {
//                        val googleClient = googleSignInViewModel.getGoogleSignInClient()
//                        googleLauncher.launch(googleClient.signInIntent)
//                    }
//                }
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            TextButton(onClick = onNavigateToRegister) {
//                Text("Bạn chưa có tài khoản? Đăng ký")
//            }
//            if (authState is AuthState.Error) {
//                Text(authState.message, color = MaterialTheme.colorScheme.error)
//            }
//            if (googleSignInState is GoogleSignInState.Error) {
//                Text(googleSignInState.message, color = MaterialTheme.colorScheme.error)
//            }
//        }
//    }
//
//    // Navigate on success
//    LaunchedEffect(authState) {
//        if (authState is AuthState.Success) {
//            authViewModel.resetState()
//            onLoginSuccess()
//        }
//    }
//    LaunchedEffect(googleSignInState) {
//        if (googleSignInState is GoogleSignInState.Success) {
//            googleSignInViewModel.resetState()
//            onLoginSuccess()
//        }
//    }
//}
// app/src/main/java/com/mb_an/tour_booking/presentation/screens/auth/LoginScreen.kt
package com.mb_an.tour_booking.presentation.screens.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.mb_an.tour_booking.R

@OptIn(ExperimentalMaterial3Api::class)
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

    val context = LocalContext.current
    val activity = context as Activity

    // Google Sign-In launcher
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
        } catch(e: Exception) {
            errorMsg = e.localizedMessage
        }
    }

    // Build GoogleSignInClient
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
    val googleClient = GoogleSignIn.getClient(activity, gso)

    Scaffold { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))

            Text(
                "Fast Travel",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))
            Text(
                "Create an account\nEnter your email to sign up for this app",
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
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.weight(1f))
                Text("  or  ", color = Color.Gray)
                Divider(modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(16.dp))
            // Google button
            OutlinedButton(
                onClick = { launcher.launch(googleClient.signInIntent) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_google_logo),
                    contentDescription = "Google"
                )
                Spacer(Modifier.width(8.dp))
                Text("Continue with Google")
            }

            Spacer(Modifier.height(12.dp))
            // Apple button (tạm icon mặc định)
            OutlinedButton(
                onClick = { /* TODO: Apple Sign-In*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_apple_logo),
                    contentDescription = "Apple"
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

            errorMsg?.let {
                Spacer(Modifier.height(12.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }

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

