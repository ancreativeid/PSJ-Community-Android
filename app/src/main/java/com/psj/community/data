package com.psj.community.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.psj.community.data.model.UserAccount
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository {

    private val auth = FirebaseAuth.getInstance()

    private val database =
        FirebaseDatabase.getInstance().reference

    suspend fun login(
        email: String,
        password: String
    ): Result<UserAccount> {

        return try {
            val result = auth.signInWithEmailAndPassword(
                email.trim(),
                password
            ).await()

            val uid = result.user?.uid
                ?: throw Exception("UID akun tidak ditemukan")

            val snapshot = database
                .child("accounts")
                .child(uid)
                .get()
                .await()

            if (!snapshot.exists()) {
                auth.signOut()
                throw Exception("Data akun belum terdaftar di PSJ.")
            }

            val account =
                snapshot.getValue(UserAccount::class.java)
                    ?: throw Exception("Data profil akun tidak valid.")

            if (!account.status.equals("active", ignoreCase = true)) {
                auth.signOut()
                throw Exception("Akun tidak aktif. Hubungi Admin PSJ.")
            }

            Result.success(account)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun currentUid(): String? {
        return auth.currentUser?.uid
    }
}
