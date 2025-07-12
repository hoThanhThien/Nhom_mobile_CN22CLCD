package com.mb_an.tour_booking.data.models

import com.google.firebase.Timestamp

data class BookingModel(
    var id: String           = "",
    val userId: String       = "",
    val tourId: String       = "",
    val tourTitle: String    = "",
    val date: String         = "",
    val timestamp: Timestamp?= null,

    val tour: TourModel   = TourModel(),
    val nights: Int          = 0,
    val quantity: Int        = 0,
    val totalPrice: Int      = 0,
    val status: String       = ""
)
