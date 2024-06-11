package com.householdshopper.model.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.householdshopper.model.ShoppingListItem
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ShoppingListItemsRepository @Inject constructor(){
    private val db = FirebaseFirestore.getInstance()

     fun addItemToList(listId: String, name: String, quantity: Int, unit: String){
        val item = ShoppingListItem(name = name, quantity =  quantity, unit =  unit)
        db.collection("shoppingLists")
            .document(listId)
            .collection("items")
            .add(item)
            .addOnSuccessListener { documentReference ->
                println("Item added to list with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                println("Error adding item to list: $e")
            }
    }
}