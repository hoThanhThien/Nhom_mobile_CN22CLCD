class BookingRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val bookingRef = firestore.collection("bookings")

    fun bookTour(tour: TourModel, userId: String, onResult: (Boolean) -> Unit) {
        val bookingData = hashMapOf(
            "userId" to userId,
            "tourId" to tour.id,
            "tourTitle" to tour.title,
            "date" to tour.date,
            "timestamp" to FieldValue.serverTimestamp()
        )

        bookingRef.add(bookingData)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }
}
