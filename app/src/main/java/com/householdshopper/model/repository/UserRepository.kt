package com.householdshopper.model.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.householdshopper.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()
    suspend fun getUser(uid: String):User?{
        return try {
            val document = db.collection("Users").document(uid)
                .get()
                .await()

            if (document.exists()){
                document.toObject(User::class.java)
            } else {
                null
            }
        }catch (e: Exception){
            println("Error has occurred")
            null
        }
    }
}