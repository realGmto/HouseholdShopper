package com.householdshopper.model.repository

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.householdshopper.model.Household
import com.householdshopper.model.ShoppingList
import com.householdshopper.model.ShoppingListItem
import com.householdshopper.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ShoppingListRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()

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

    suspend fun getSpecificShoppingList(listID:String): ShoppingList?{
        return try {
            val document = db.collection("ShoppingLists")
                .document(listID)
                .get()
                .await()

            if (document.exists()) {
                val shoppingList = document.toObject(ShoppingList::class.java)?.apply {
                    this.documentId = document.id // Set the documentId
                }

                val itemsSnapshot = document.reference.collection("items")
                    .get()
                    .await()

                val items = itemsSnapshot.documents.mapNotNull { itemDoc ->
                    itemDoc.toObject(ShoppingListItem::class.java)?.apply {
                        documentId = itemDoc.id // Set the documentId
                    }
                }

                shoppingList?.copy(items = items)
            } else {
                null
            }
        } catch (e: Exception){
            println("Error getting shopping lists: $e")
            null
        }
    }

    suspend fun createNewList(user: User,household: Household,name: String):String{
        //TODO
        return try {
            val shoppingList = hashMapOf(
                "assignedUserID" to user.documentId,
                "creationDate" to Timestamp.now(),
                "householdID" to household.householdId,
                "name" to name
            )
            val document = db.collection("ShoppingLists")
                .add(shoppingList)
                .await()

            document.id
        }catch (e:Exception){
            println("Error creating shopping list: $e")
            ""
        }
    }

    // Needs to be moved to ShoppingListItem repository
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