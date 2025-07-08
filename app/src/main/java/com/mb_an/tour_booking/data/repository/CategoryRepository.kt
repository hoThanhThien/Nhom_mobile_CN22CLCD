
package com.mb_an.tour_booking.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.mb_an.tour_booking.data.models.CategoryModel

class CategoryRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val col = firestore.collection("categories")

    fun fetchCategories(onResult: (List<CategoryModel>) -> Unit) {
        col.get()
            .addOnSuccessListener { snap ->
                val list = snap.documents.mapNotNull { doc ->
                    // doc.toObject sẽ dùng cái default constructor bên trên
                    doc.toObject(CategoryModel::class.java)?.copy(id = doc.id)
                }
                onResult(list)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
}
