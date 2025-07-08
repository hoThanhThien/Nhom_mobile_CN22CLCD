class AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun loginWithEmail(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener {
                onResult(false, it.localizedMessage)
            }
    }

    fun getCurrentUserId(): String? = auth.currentUser?.uid
}
