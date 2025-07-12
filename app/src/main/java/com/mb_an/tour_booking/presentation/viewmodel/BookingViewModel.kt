// app/src/main/java/com/mb_an/tour_booking/presentation/viewmodel/BookingViewModel.kt
package com.mb_an.tour_booking.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mb_an.tour_booking.data.models.BookingModel
import com.mb_an.tour_booking.data.models.TourModel
import com.mb_an.tour_booking.data.repository.AuthRepository
import com.mb_an.tour_booking.data.repository.BookingRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class BookingViewModel(
    private val repository: BookingRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    // 1. Danh sách booking của user
    var bookingList by mutableStateOf<List<BookingModel>>(emptyList())
        private set

    // 2. Trạng thái hành động (đặt/hủy)
    var bookingState by mutableStateOf<BookingState>(BookingState.Idle)
        private set

//    init {
//        loadBookings()
//    }

    /** Nạp lại danh sách booking */
    fun loadBookings() {
        val userId = authRepository.getCurrentUserId() ?: return
        Log.d("BookingVM", "loadBookings userId=$userId")
        repository.fetchUserBookings(userId) { list ->
            Log.d("BookingVM", "  callback list.size=${list.size}")
            bookingList = list
        }
    }

    /** Đặt tour mới */
    fun bookTour(
        tour: TourModel,
        startDate: LocalDate,
        endDate: LocalDate,
        guests: Int) {
        val userId = authRepository.getCurrentUserId()
        if (userId == null) {
            bookingState = BookingState.Error("Chưa đăng nhập")
            return
        }
        bookingState = BookingState.Loading
        repository.bookTour(
            tour = tour,
            userId = userId,
            startDate = startDate,
            endDate = endDate,
            guests = guests
        ) { success ->
            bookingState = if (success) BookingState.Success
            else BookingState.Error("Đặt tour thất bại")
            if (success) loadBookings()
        }
    }

    /** Hủy 1 booking */
    fun cancelBooking(booking: BookingModel) {
        bookingState = BookingState.Loading
        repository.cancelBooking(booking.id) { success ->
            bookingState = if (success) BookingState.Success else BookingState.Error("Hủy tour thất bại")
            if (success) loadBookings()
        }
    }

    /** Reset về Idle sau khi xử lý xong */
    fun resetState() {
        bookingState = BookingState.Idle
    }

}

sealed class BookingState {
    object Idle    : BookingState()
    object Loading : BookingState()
    object Success : BookingState()
    data class Error(val message: String) : BookingState()
}
