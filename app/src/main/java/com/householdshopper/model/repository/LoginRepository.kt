package com.householdshopper.model.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.householdshopper.model.LoginResult
import kotlinx.coroutines.tasks.await

class LoginRepository {
    private val auth: FirebaseAuth = Firebase.auth

    suspend fun login(email: String, password: String): LoginResult {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            LoginResult(success = true, message = "Login successful")
        } catch (e: Exception) {
            LoginResult(success = false, message = "Wrong email or password")
        }
    }
}