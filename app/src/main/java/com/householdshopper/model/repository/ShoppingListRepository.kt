package com.householdshopper.model.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.householdshopper.model.ShoppingList
import com.householdshopper.model.ShoppingListItem
import kotlinx.coroutines.tasks.await

class ShoppingListRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getActiveShoppingLists(householdID: String): List<ShoppingList>{
        return try {
            val shoppingListsSnapshot = db.collection("ShoppingLists")
                .whereEqualTo("householdID",householdID)
                .whereEqualTo("isActive",true)
                .get()
                .await()

            shoppingListsSnapshot.documents.mapNotNull { document ->
                val shoppingList = try {
                    document.toObject(ShoppingList::class.java)?.apply {
                        documentId = document.id // Set the documentId
                    }
                } catch (e: Exception) {
                    Log.e("Firestore", "Error converting document to ShoppingList", e)
                    null
                }
                shoppingList?.let {
                    val itemsSnapshot = try {
                        document.reference.collection("items")
                            .get()
                            .await()
                    } catch (e: Exception) {
                        Log.e("Firestore", "Error fetching items subcollection for document ${document.id}", e)
                        return@let null
                    }

                    val items = itemsSnapshot.documents.mapNotNull { itemDoc ->
                        Log.d("Firestore", "Testing $itemDoc")
                        try {
                            itemDoc.toObject(ShoppingListItem::class.java)?.apply {
                                documentId = itemDoc.id // Set the documentId
                            }
                        } catch (e: Exception) {
                            Log.e("Firestore", "Error converting item document to ShoppingListItem", e)
                            null
                        }
                    }

                    shoppingList.copy(items = items)
                }
            }
        } catch (e: Exception) {
            println("Error getting shopping lists: $e")
            emptyList()
        }
    }

    suspend fun getAllShoppingLists(householdID: String): List<ShoppingList>{
        return try {
            val shoppingListsSnapshot = db.collection("ShoppingLists")
                .whereEqualTo("householdID",householdID)
                .get()
                .await()

            shoppingListsSnapshot.documents.mapNotNull { document ->
                val shoppingList = try {
                    document.toObject(ShoppingList::class.java)?.apply {
                        documentId = document.id // Set the documentId
                    }
                } catch (e: Exception) {
                    Log.e("Firestore", "Error converting document to ShoppingList", e)
                    null
                }
                shoppingList?.let {
                    val itemsSnapshot = try {
                        document.reference.collection("items")
                            .get()
                            .await()
                    } catch (e: Exception) {
                        Log.e("Firestore", "Error fetching items subcollection for document ${document.id}", e)
                        return@let null
                    }

                    val items = itemsSnapshot.documents.mapNotNull { itemDoc ->
                        Log.d("Firestore", "Testing $itemDoc")
                        try {
                            itemDoc.toObject(ShoppingListItem::class.java)?.apply {
                                documentId = itemDoc.id // Set the documentId
                            }
                        } catch (e: Exception) {
                            Log.e("Firestore", "Error converting item document to ShoppingListItem", e)
                            null
                        }
                    }

                    shoppingList.copy(items = items)
                }
            }
        } catch (e: Exception) {
            println("Error getting shopping lists: $e")
            emptyList()
        }
    }
    // Needs to be updated
    fun addItemToList(listId: String, itemId: String, quantity: Int, unit: String){
        val item = ShoppingListItem(itemID = itemId, quantity =  quantity, unit =  unit)
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

    suspend fun getItemsFromList(listId: String): List<ShoppingListItem> {
        return try {
            val result = db.collection("shoppingLists")
                .document(listId)
                .collection("items")
                .get()
                .await()

            result.documents.mapNotNull { it.toObject(ShoppingListItem::class.java) }
        } catch (e: Exception) {
            println("Error getting items: $e")
            emptyList()
        }
    }
}