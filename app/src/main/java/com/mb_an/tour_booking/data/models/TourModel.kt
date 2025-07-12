package com.mb_an.tour_booking.data.models

data class TourModel(
    var id: String = "",
    val title: String = "",
    val date: String = "",
    val location: String = "",
    val price: Int = 0,
    val imageUrl: String = "",
    val description: String = "",
    val rating: Double = 0.0,
    val images: List<String> = emptyList(),
    val reviewCount: Int      = 0,
    val bannerUrls: List<String> = emptyList(),
)
{
    // helper trả về list ảnh để slider dùng
    fun getImageList(): List<String> =
        if (images.isEmpty()) listOf(imageUrl) else images
}


