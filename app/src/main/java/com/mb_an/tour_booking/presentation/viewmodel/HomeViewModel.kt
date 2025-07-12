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
    //Querry
    var query by mutableStateOf("")
        private set
    // Tours
    var tourList by mutableStateOf<List<TourModel>>(emptyList())
        private set
    //Danh sách tour đã lọc
    val filteredTours: List<TourModel>
        get() = if (query.isBlank()) tourList
        else tourList.filter {
            it.title.contains(query, ignoreCase = true)
        }
    // Lấy banner URL từ tour đầu tiên, nếu có
    val bannerUrls: List<String>
        get() = tourList.firstOrNull()?.bannerUrls ?: emptyList()

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
    //Update querry khi người dùng đánh từ khóa**
    fun onQueryChanged(newQuery: String) {
        query = newQuery
    }
}
