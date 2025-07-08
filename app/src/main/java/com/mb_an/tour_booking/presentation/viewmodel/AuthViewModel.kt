package com.mb_an.tour_booking.presentation.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.mb_an.tour_booking.data.repository.AuthRepository

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    var authState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    fun login(email: String, password: String) {
        authState = AuthState.Loading
        repository.loginWithEmail(email, password) { user, error ->
            authState = if (user != null) {
                AuthState.Success(user)
            } else {
                AuthState.Error(error ?: "Đăng nhập thất bại")
            }
        }
    }

    fun register(email: String, password: String) {
        authState = AuthState.Loading
        repository.registerWithEmail(email, password) { user, error ->
            authState = if (user != null) {
                AuthState.Success(user)
            } else {
                AuthState.Error(error ?: "Đăng ký thất bại")
            }
        }
    }

    fun resetPassword(email: String) {
        authState = AuthState.Loading
        repository.sendPasswordReset(email) { success, error ->
            authState = if (success) {
                AuthState.PasswordResetSent
            } else {
                AuthState.Error(error ?: "Gửi email thất bại")
            }
        }
    }

    fun resetState() {
        authState = AuthState.Idle
    }
    fun setError(message: String) {
        authState = AuthState.Error(message)
    }

}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: FirebaseUser) : AuthState()
    object PasswordResetSent : AuthState()
    data class Error(val message: String) : AuthState()
}
