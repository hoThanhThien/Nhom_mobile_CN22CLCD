class TourRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val tourCollection = firestore.collection("tours")

    fun fetchTours(onResult: (List<TourModel>) -> Unit) {
        tourCollection.get()
            .addOnSuccessListener { snapshot ->
                val tours = snapshot.documents.mapNotNull { it.toObject(TourModel::class.java) }
                onResult(tours)
            }
            .addOnFailureListener {
                onResult(emptyList()) // TODO: Có thể xử lý lỗi chi tiết hơn nếu cần
            }
    }
}
