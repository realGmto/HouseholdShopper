package com.householdshopper.model.repository

import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.householdshopper.model.RegisterResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RegisterRepository @Inject constructor() {

    suspend fun register(
        email: String,
        password: String,
        username: String,
        householdId: String
    ): RegisterResult {
        return try {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val user = hashMapOf(
                "username" to username,
                "householdID" to householdId
            )

            val db = FirebaseFirestore.getInstance()

            if (userId != null) {
                db.collection("Users").document(userId)
                    .set(user)
                    .await()
            }
            else{
                RegisterResult(success = false, message = "Failed to store user data")
            }
            RegisterResult(success = true, message = "Registered successfully")
        } catch (e: Exception){
            RegisterResult(success = false, message = "Failed to register")
        }
    }
}