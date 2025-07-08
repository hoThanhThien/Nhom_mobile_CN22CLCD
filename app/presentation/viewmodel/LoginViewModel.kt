class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    var loginState by mutableStateOf<LoginState>(LoginState.Idle)
        private set

    fun login(email: String, password: String) {
        loginState = LoginState.Loading
        repository.loginWithEmail(email, password) { success, error ->
            loginState = if (success) LoginState.Success
            else LoginState.Error(error ?: "Đăng nhập thất bại")
        }
    }
}

// State dùng để cập nhật UI
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}
