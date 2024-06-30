package com.householdshopper.model.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.householdshopper.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()
    private val collectionPath = "Users"

    suspend fun getAllUsers():List<User>{
        return try {
            val document = db.collection(collectionPath)
                .get()
                .await()

            document.documents.mapNotNull { doc ->
                doc.toObject(User::class.java)?.apply { documentId = doc.id }
            }
        }catch (e: Exception){
            println("Error while getting users")
            emptyList()
        }
    }

    fun getAllUsersUpdates():Flow<List<User>> = callbackFlow{
        val listenerRegistration: ListenerRegistration = db.collection(collectionPath)
            .addSnapshotListener{snapshot, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }

                val users = snapshot?.documents?.mapNotNull { doc->
                    doc.toObject(User::class.java)?.apply { documentId = doc.id }
                }?: emptyList()

                trySend(users).isSuccess
            }
        awaitClose{ listenerRegistration.remove() }
    }

    fun getSpecificUsersUpdates(householdID: String): Flow<List<User>> = callbackFlow{
        val listenerRegistration: ListenerRegistration = db.collection(collectionPath)
            .whereEqualTo("householdID",householdID)
            .addSnapshotListener{snapshot, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }

                val users = snapshot?.documents?.mapNotNull { doc->
                    doc.toObject(User::class.java)?.apply { documentId = doc.id }
                }?: emptyList()

                trySend(users).isSuccess
            }
        awaitClose{ listenerRegistration.remove() }
    }
    suspend fun getUser(uid: String):User{
        return try {
            val document = db.collection(collectionPath).document(uid)
                .get()
                .await()

            document.toObject(User::class.java)?.apply {
                documentId = document.id
            }?: User()
        }catch (e: Exception){
            println("Error has occurred")
            User()
        }
    }

    suspend fun updateHousehold(userID: String, householdID: String):Boolean{
        return try {
            db.collection(collectionPath)
                .document(userID)
                .update("householdID",householdID)
                .await()
            true
        }catch (e:Exception){
            println(e.message)
            false
        }
    }
}