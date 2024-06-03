package com.householdshopper.model.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.householdshopper.model.RegisterResult
import kotlinx.coroutines.tasks.await

class RegisterRepository {
    private val auth: FirebaseAuth = Firebase.auth

    suspend fun register(email: String, password: String): RegisterResult {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            RegisterResult(success = true, message = "Registration successful")
        } catch (e: Exception) {
            RegisterResult(success = false, message = e.message ?: "Registration failed")
        }
    }
}