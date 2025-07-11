
package com.mb_an.tour_booking.data.models

// Các property phải có default value để Firestore có thể khởi tạo
data class CategoryModel(
    var id: String = "",
    val name: String = "",
    val imageUrl: String = ""
)
