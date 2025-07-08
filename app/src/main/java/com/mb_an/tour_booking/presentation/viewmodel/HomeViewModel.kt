// app/src/main/java/com/mb_an/tour_booking/presentation/viewmodel/HomeViewModel.kt
package com.mb_an.tour_booking.presentation.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.mb_an.tour_booking.data.models.CategoryModel
import com.mb_an.tour_booking.data.models.TourModel
import com.mb_an.tour_booking.data.repository.CategoryRepository
import com.mb_an.tour_booking.data.repository.TourRepository

class HomeViewModel(
    private val tourRepo: TourRepository,
    private val catRepo:  CategoryRepository
) : ViewModel() {

    // Tours
    var tourList by mutableStateOf<List<TourModel>>(emptyList())
        private set

    // Categories
    var categories by mutableStateOf<List<CategoryModel>>(emptyList())
        private set

    // Loading / error flags (tuỳ bạn có dùng hay không)
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadTours()
        loadCategories()
    }

    fun loadTours() {
        isLoading = true
        tourRepo.fetchTours { tours ->
            isLoading = false
            if (tours.isNotEmpty()) tourList = tours
            else errorMessage = "Không tìm thấy tour nào"
        }
    }

    fun loadCategories() {
        catRepo.fetchCategories { cats ->
            categories = cats
        }
    }
}
