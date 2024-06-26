package com.householdshopper.model.repository

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.google.auth.oauth2.AccessToken
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.householdshopper.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.FileInputStream
import java.io.IOException
import java.util.Arrays
import javax.inject.Inject


class FirebaseMessageRepository @Inject constructor() {
    fun getTokenAndSendToServer() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d(TAG, "FCM Token: $token")

            // Send token to your server
            sendTokenToServer(token)
        }
    }

    fun sendTokenToServer(token: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("Users").document(userId)
            .update("token",token)
            .addOnSuccessListener {
                Log.d("FCM", "Token successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w("FCM", "Error writing token", e)
            }
    }


    @Throws(IOException::class)
    private fun getAccessToken(context: Context): String {
        val scopes = listOf("https://www.googleapis.com/auth/firebase.messaging")
        val InputStream =context.resources.openRawResource(R.raw.householdshopper)

        val googleCredentials: GoogleCredentials = GoogleCredentials
            .fromStream(InputStream)
            .createScoped(scopes)

        googleCredentials.refreshIfExpired()
        return googleCredentials.accessToken.tokenValue
    }

    suspend fun sendNotification(userId: String, title: String, body: String, context: Context) {
        val db = FirebaseFirestore.getInstance()
        try {
            // Fetch the document on a background thread
            val documentSnapshot = withContext(Dispatchers.IO) {
                db.collection("Users").document(userId).get().await()
            }
            val token = documentSnapshot.getString("token")
            if (!token.isNullOrEmpty()) {
                val message = JSONObject().apply {
                    put("token", token)
                    put("notification", JSONObject().apply {
                        put("body", body)
                        put("title", title)
                    })
                }
                val json = JSONObject().apply {
                    put("message", message)
                }

                val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
                val requestBody = json.toString().toRequestBody(JSON)

                // Get the access token on a background thread
                val accessToken = withContext(Dispatchers.IO) {
                    getAccessToken(context)
                }

                // Create and execute the network request on a background thread
                withContext(Dispatchers.IO) {
                    val request = Request.Builder()
                        .url("https://fcm.googleapis.com/v1/projects/household-shopper-12028/messages:send")
                        .addHeader("Authorization", "Bearer $accessToken")
                        .addHeader("Content-Type", "application/json")
                        .post(requestBody)
                        .build()

                    val client = OkHttpClient()
                    client.newCall(request).enqueue(object : okhttp3.Callback {
                        override fun onFailure(call: okhttp3.Call, e: IOException) {
                            Log.e("FCM", "Failed to send notification", e)
                        }

                        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                            if (response.isSuccessful) {
                                Log.d("FCM", "Notification sent successfully")
                            } else {
                                Log.e("FCM", "Error sending notification: ${response.code}")
                            }
                        }
                    })
                }
            } else {
                Log.d("FCM", "No token found for user")
            }
        } catch (e: Exception) {
            Log.e("FCM", "Failed to send notification", e)
        }
    }
}