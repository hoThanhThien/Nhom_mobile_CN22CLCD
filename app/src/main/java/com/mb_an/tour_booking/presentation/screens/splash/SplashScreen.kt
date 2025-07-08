package com.mb_an.tour_booking.presentation.screens.splash

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SplashScreen(onAuthCheck: (Boolean) -> Unit) {
    // Hiện progress trong 1.5s rồi gọi callback
    LaunchedEffect(Unit) {
        delay(1500)
        val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
        onAuthCheck(isLoggedIn)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
