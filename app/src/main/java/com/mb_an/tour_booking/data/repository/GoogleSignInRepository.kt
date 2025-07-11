package com.mb_an.tour_booking.data.repository
import com.mb_an.tour_booking.R
import android.app.Activity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleSignInRepository(private val activity: Activity) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // 1) Tạo GoogleSignInClient duy nhất
    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        // DÙNG OAuth web client ID, không phải API key
        .requestIdToken(activity.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    private val client: GoogleSignInClient
        get() = GoogleSignIn.getClient(activity, gso)
    /** Trả về client để launch Sign-In Intent */
    fun getGoogleSignInClient(): GoogleSignInClient = client

    /** Đăng xuất tài khoản Google hiện tại (giúp show chooser khi next launch) */
    fun signOut(onComplete: (() -> Unit)? = null) {
        client.signOut().addOnCompleteListener {
            onComplete?.invoke()
        }
    }

    /** Thu hồi hoàn toàn quyền truy cập (nếu bạn muốn) */
    fun revokeAccess(onComplete: (() -> Unit)? = null) {
        client.revokeAccess().addOnCompleteListener {
            onComplete?.invoke()
        }
    }
//    Sau khi user chọn account, gọi hàm này để lấy credential
//    và authenticate với Firebase.

    fun firebaseAuthWithGoogle(
        account: GoogleSignInAccount,
        onResult: (Boolean, String?) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.localizedMessage) }
    }
}
