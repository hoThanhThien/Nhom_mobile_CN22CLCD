package com.mb_an.tour_booking.data.models

data class TourModel(
    val id: String = "",
    val title: String = "",
    val date: String = "",
    val location: String = "",
    val price: Int = 0,
    val imageUrl: String = "",
    val description: String = "",
    val rating: Double = 0.0,
    val images: List<String>
)


