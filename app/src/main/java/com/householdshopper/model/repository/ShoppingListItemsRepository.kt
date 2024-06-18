package com.householdshopper.model.repository

import android.util.Log
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.householdshopper.model.ShoppingListItem
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ShoppingListItemsRepository @Inject constructor(){
    private val db = FirebaseFirestore.getInstance()

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
}