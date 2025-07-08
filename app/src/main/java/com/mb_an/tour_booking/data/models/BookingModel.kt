package com.mb_an.tour_booking.data.models

import com.google.firebase.Timestamp

data class BookingModel(
    val id: String = "",
    val userId: String = "",
    val tourId: String = "",
    val tourTitle: String = "",
    val date: String = "",
    val timestamp: Timestamp? = null
)
