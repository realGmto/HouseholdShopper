package com.householdshopper.model.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.householdshopper.model.ShoppingList
import com.householdshopper.model.ShoppingListItem
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ShoppingListItemsRepository @Inject constructor(){
    private val db = FirebaseFirestore.getInstance()

    fun getItemsUpdates(listId: String):Flow<List<ShoppingListItem>> = callbackFlow{
        val listenerRegistration: ListenerRegistration = Firebase.firestore.collection("ShoppingLists")
            .document(listId)
            .collection("items")
            .addSnapshotListener(MetadataChanges.INCLUDE) { snapshots, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }

                val itemsList = snapshots?.documents?.mapNotNull { doc ->
                    doc.toObject<ShoppingListItem>()
                } ?: emptyList()

                trySend(itemsList).isSuccess
            }
        awaitClose { listenerRegistration.remove() }
    }

     suspend fun addItemToList(listId: String, name: String, quantity: Int, unit: String): Boolean{
        val item = ShoppingListItem(name = name, quantity =  quantity, unit =  unit)
         return try {
             val result = db.collection("ShoppingLists")
                 .document(listId)
                 .collection("items")
                 .add(item)
                 .await()
             Log.d("Firestore", "Successfully added item with ID: ${result.id}")
             true
         } catch (e:Exception){
             Log.e("Firestore", "Error adding new item", e)
             false
         }
    }

    suspend fun removeItemFromList(listId: String,documentId: String): Boolean{
        return try {
            val result = db.collection("ShoppingLists")
                .document(listId)
                .collection("items")
                .document(documentId)
                .delete()
                .await()
            Log.d("Firestore", "Successfully removed item from list")
            true
        }catch (e:Exception){
            Log.e("Firestore", "Error while removing item", e)
            false
        }
    }

    suspend fun updateIsBought(listId: String, documentId: String,state : Boolean): Boolean {
        return try {
            db.collection("ShoppingLists")
                .document(listId)
                .collection("items")
                .document(documentId)
                .update("isBought",state)
                .await()
            Log.d("Firestore", "Successfully updated isBought field in item")
            true
        }catch (e:Exception){
            Log.e("Firestore", "Error while updating isBought field in item", e)
            false
        }
    }
}