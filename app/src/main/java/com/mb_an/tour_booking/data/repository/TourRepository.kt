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
            val tours = snapshot.documents.mapNotNull { doc ->
                // 1) parse -> 2) gán id -> 3) trả về
                doc.toObject(TourModel::class.java)
                    ?.apply { id = doc.id }      // ← tuyệt đối không được bỏ dòng này
            }
            onResult(tours)
        }
        .addOnFailureListener {
            onResult(emptyList())
        }
}

}

