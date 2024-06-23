package com.householdshopper.model.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.firestore
import com.householdshopper.model.Household
import com.householdshopper.model.ShoppingList
import com.householdshopper.model.ShoppingListItem
import com.householdshopper.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ShoppingListRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()
    private val collectionPath = "ShoppingLists"

    fun getShoppingListsUpdates(householdID: String): Flow<List<ShoppingList>> = callbackFlow {
        val listenerRegistration: ListenerRegistration = db.collection(collectionPath)
            .whereEqualTo("householdID",householdID)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }

                val shoppingLists = snapshots?.documents?.map { doc ->
                    doc.toObject(ShoppingList::class.java)?.apply { documentId = doc.id }
                }?.filterNotNull() ?: emptyList()

                trySend(shoppingLists).isSuccess
            }
        awaitClose { listenerRegistration.remove() }
    }

    fun getSpecificShoppingListUpdates(listId: String):Flow<ShoppingList> = callbackFlow{
        val listenerRegistration: ListenerRegistration = Firebase.firestore.collection("ShoppingLists")
            .document(listId)
            .addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }
                val shoppingList = snapshot?.toObject(ShoppingList::class.java)?.apply {
                    documentId = snapshot.id
                } ?: ShoppingList()

                trySend(shoppingList).isSuccess
        }
        awaitClose { listenerRegistration.remove() }
    }

    suspend fun createNewList(user: User,household: Household,name: String):String{
        return try {
            val shoppingList = hashMapOf(
                "assignedUserID" to user.documentId,
                "creationDate" to Timestamp.now(),
                "householdID" to household.householdId,
                "name" to name
            )
            val document = db.collection(collectionPath)
                .add(shoppingList)
                .await()

            document.id
        }catch (e:Exception){
            println("Error creating shopping list: $e")
            ""
        }
    }
}