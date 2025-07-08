package com.mb_an.tour_booking.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.mb_an.tour_booking.data.models.TourModel

class TourRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val tourCollection = firestore.collection("tours")

//    fun fetchTours(onResult: (List<TourModel>) -> Unit) {
//        tourCollection.get()
//            .addOnSuccessListener { snapshot ->
//                val tours = snapshot.documents.mapNotNull {
//                    it.toObject(TourModel::class.java)
//                }
//                onResult(tours) // ✅ đảm bảo trả về List<TourModel>
//            }
//            .addOnFailureListener {
//                onResult(emptyList()) // ✅ trả về List rỗng khi lỗi
//            }
//    }
fun fetchTours(onResult: (List<TourModel>) -> Unit) {
    tourCollection.get()
        .addOnSuccessListener { snapshot ->
            // mapNotNull phải trả về TourModel? cho mỗi document
            val tours = snapshot.documents.mapNotNull { doc ->
                // Chuyển document về TourModel, nếu null thì bỏ qua
                doc.toObject(TourModel::class.java)?.also { model ->
                    Log.d("TourRepository", "Loaded tour: id=${model.id}, title=${model.title}")
                }
            }
            Log.d("TourRepository", "Total tours fetched: ${tours.size}")
            onResult(tours)
        }
        .addOnFailureListener { e ->
            Log.e("TourRepository", "Error fetching tours", e)
            onResult(emptyList())
        }
}
}
