package com.mb_an.tour_booking.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.mb_an.tour_booking.data.models.BookingModel
import com.mb_an.tour_booking.data.models.TourModel
import com.google.firebase.firestore.FieldValue

class BookingRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val bookingRef = firestore.collection("bookings")

    /** Tạo 1 booking mới */
    fun bookTour(
        tour: TourModel,
        userId: String,
        onResult: (Boolean) -> Unit
    ) {
        val bookingData = hashMapOf(
            "userId"    to userId,
            "tourId"    to tour.id,
            "tourTitle" to tour.title,
            "date"      to tour.date,
            "timestamp" to FieldValue.serverTimestamp()
        )
        bookingRef
            .add(bookingData)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    /** Lấy danh sách booking của user */
    fun fetchUserBookings(
        userId: String,
        onResult: (List<BookingModel>) -> Unit
    ) {
        bookingRef
            .whereEqualTo("userId", userId)
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { snap: QuerySnapshot ->
                val list = snap.documents.mapNotNull {
                    it.toObject(BookingModel::class.java)?.copy(id = it.id)
                }
                onResult(list)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    /** Hủy 1 booking */
    fun cancelBooking(
        bookingId: String,
        onResult: (Boolean) -> Unit
    ) {
        bookingRef
            .document(bookingId)
            .delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }
}
