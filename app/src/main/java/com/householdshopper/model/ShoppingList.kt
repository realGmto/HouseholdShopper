package com.householdshopper.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId


data class ShoppingList (
    val assignedUserID: String? = null,
    val creationDate: Timestamp? = null,
    val householdID: String = "",
    val name: String = "",
    var items: List<ShoppingListItem> = emptyList(),
    var documentId: String = ""
)