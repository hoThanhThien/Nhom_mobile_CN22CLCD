package com.mb_an.tour_booking.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun loginWithEmail(
        email: String,
        password: String,
        onResult: (FirebaseUser?, String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { onResult(it.user, null) }
            .addOnFailureListener { e -> onResult(null, e.localizedMessage) }
    }

    fun registerWithEmail(
        email: String,
        password: String,
        onResult: (FirebaseUser?, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { onResult(it.user, null) }
            // Khi tạo tài khoản thành công, authResult.user chứa FirebaseUser mới
            .addOnFailureListener { e -> onResult(null, e.localizedMessage) }
    }

    fun sendPasswordReset(email: String, onResult: (Boolean, String?) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.localizedMessage) }
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    fun logout() {
        auth.signOut()
    }
}
