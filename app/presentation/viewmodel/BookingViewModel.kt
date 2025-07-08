class BookingViewModel(
    private val repository: BookingRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    var bookingState by mutableStateOf<BookingState>(BookingState.Idle)
        private set

    fun bookTour(tour: TourModel) {
        val userId = authRepository.getCurrentUserId()
        if (userId == null) {
            bookingState = BookingState.Error("Chưa đăng nhập")
            return
        }

        bookingState = BookingState.Loading
        repository.bookTour(tour, userId) { success ->
            bookingState = if (success) BookingState.Success
            else BookingState.Error("Đặt tour thất bại")
        }
    }
}

sealed class BookingState {
    object Idle : BookingState()
    object Loading : BookingState()
    object Success : BookingState()
    data class Error(val message: String) : BookingState()
}
