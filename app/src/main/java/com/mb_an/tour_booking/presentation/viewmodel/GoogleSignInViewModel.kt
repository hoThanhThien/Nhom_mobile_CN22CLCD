package com.mb_an.tour_booking.presentation.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.mb_an.tour_booking.data.repository.GoogleSignInRepository
import org.koin.core.parameter.parametersOf

class GoogleSignInViewModel(private val repository: GoogleSignInRepository) : ViewModel() {

    var googleSignInState by mutableStateOf<GoogleSignInState>(GoogleSignInState.Idle)
        private set

    fun signInWithGoogle(account: GoogleSignInAccount) {
        googleSignInState = GoogleSignInState.Loading
        repository.firebaseAuthWithGoogle(account) { success, error ->
            googleSignInState = if (success) {
                GoogleSignInState.Success
            } else {
                GoogleSignInState.Error(error ?: "Đăng nhập Google thất bại")
            }
        }
    }

    fun getGoogleSignInClient() = repository.getGoogleSignInClient()

    fun resetState() {
        googleSignInState = GoogleSignInState.Idle
    }

}

sealed class GoogleSignInState {
    object Idle : GoogleSignInState()
    object Loading : GoogleSignInState()
    object Success : GoogleSignInState()
    data class Error(val message: String) : GoogleSignInState()
}
