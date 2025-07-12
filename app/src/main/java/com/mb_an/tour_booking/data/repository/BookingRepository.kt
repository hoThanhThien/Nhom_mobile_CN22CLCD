package com.mb_an.tour_booking.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.mb_an.tour_booking.data.models.BookingModel
import com.mb_an.tour_booking.data.models.TourModel
import com.google.firebase.firestore.FieldValue
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class BookingRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val bookingRef = firestore.collection("bookings")

    /** Tạo 1 booking mới */
    fun bookTour(
        tour: TourModel,
        userId: String,
        startDate: LocalDate,
        endDate:   LocalDate,
        guests:    Int,
        onResult:  (Boolean) -> Unit
    ) {
        // 1) Tính nights và totalPrice
        val nights     = ChronoUnit.DAYS.between(startDate, endDate).toInt()
        val totalPrice = nights * tour.price * guests
        val dateRange  = "$startDate → $endDate"

        // 2) Lưu nguyên tất cả field cũ + thêm field mới
        val bookingData = hashMapOf<String, Any?>(
            "userId"     to userId,
            "tourId"     to tour.id,
            "tourTitle"  to tour.title,
            "date"       to dateRange,              // giữ nguyên key "date"
            "timestamp"  to FieldValue.serverTimestamp(),
            "tour"       to tour,                    // ← embed nguyên TourModel
            // bổ sung:
            "tour"       to tour,                   // embed TourModel
            "nights"     to nights,
            "quantity"   to guests,
            "totalPrice" to totalPrice,
            "status"     to "Hoàn tất"              // trạng thái
        )
        // 2) Ghi xuống Firestore
        bookingRef
            .add(bookingData)
            .addOnSuccessListener {
                Log.d("BookingRepo", "✅ bookTour success")
                onResult(true)
            }
            .addOnFailureListener { e ->
                Log.e("BookingRepo", "❌ bookTour failed", e)
                onResult(false)
            }
    }

    /** Lấy danh sách booking của user */
    fun fetchUserBookings(
        userId: String,
        onResult: (List<BookingModel>) -> Unit
    ) {
        Log.d("BookingRepo", "fetchUserBookings() for userId=$userId")
        bookingRef
            .whereEqualTo("userId", userId)
//
            .get()
            .addOnSuccessListener { snap: QuerySnapshot ->
                Log.d("BookingRepo", "  got ${snap.size()} docs")
                snap.documents.forEach { doc  ->
                    Log.d("BookingRepo", "    docId=${doc.id} → userIdInDoc=${doc.getString("userId")}")
                }
                val list = snap.documents.mapNotNull {
                    it.toObject(BookingModel::class.java)?.copy(id = it.id)
                }
                Log.d("BookingRepo", "  mapped list.size=${list.size}")
                onResult(list)
            }
            .addOnFailureListener { e ->
                Log.e("BookingRepo", "fetchUserBookings failed", e)
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
