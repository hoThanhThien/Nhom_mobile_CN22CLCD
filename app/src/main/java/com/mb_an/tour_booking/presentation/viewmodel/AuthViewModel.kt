
package com.mb_an.tour_booking.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mb_an.tour_booking.data.repository.AuthRepository
import kotlinx.coroutines.flow.*

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    // 1) StateFlow giữ currentUser
    private val _currentUser = MutableStateFlow<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()
    var authState by mutableStateOf<AuthState>(AuthState.Idle)


    // 2) StateFlow báo đang login hay chưa
    val isLoggedIn: StateFlow<Boolean> = currentUser
        .map { it != null }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = (firebaseAuth.currentUser != null)
        )
    //expose email
    val userEmail: StateFlow<String> = currentUser
        .map { user -> user?.email.orEmpty() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, firebaseAuth.currentUser?.email.orEmpty())

    init {
        // Lắng nghe thay đổi authentication
        firebaseAuth.addAuthStateListener { auth ->
            _currentUser.value = auth.currentUser
        }
    }


    // Các hàm login / register / logout …
    fun logout() {
        firebaseAuth.signOut()
        _currentUser.value = null
    }

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

