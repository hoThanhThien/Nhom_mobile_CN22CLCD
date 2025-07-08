package com.mb_an.tour_booking.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.mb_an.tour_booking.data.repository.AuthRepository

class LoginViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    var loginState by mutableStateOf<LoginState>(LoginState.Idle)
        private set

    fun login(email: String, password: String) {
        loginState = LoginState.Loading

        repository.loginWithEmail(email, password) { user: FirebaseUser?, error: String? ->
            loginState = if (user != null) {
                LoginState.Success
            } else {
                LoginState.Error(error ?: "Đăng nhập thất bại")
            }
        }
    }
    fun resetLoginState() {
        loginState = LoginState.Idle
    }
}

sealed class LoginState {
    object Idle    : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}
