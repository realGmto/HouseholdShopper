package com.householdshopper.model.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.householdshopper.model.LoginResult
import com.householdshopper.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginRepository @Inject constructor(){
    private val auth: FirebaseAuth = Firebase.auth
    private val db = FirebaseFirestore.getInstance()

    suspend fun login(email: String, password: String): LoginResult {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()

            val document = db.collection("Users").document(auth.currentUser?.uid ?: "")
                .get()
                .await()

            val result = document.toObject(User::class.java)?.apply {
                documentId = document.id
            }?: User()

            if (result.householdID != "")
                LoginResult(success = true, message = "Login successful", inHousehold = true)
            else
                LoginResult(success = true, message = "Login successful", inHousehold = false)
        } catch (e: Exception) {
            LoginResult(success = false, message = "Wrong email or password", inHousehold = false)
        }
    }
}