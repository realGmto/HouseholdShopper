package com.householdshopper.model

import com.google.firebase.firestore.DocumentId

data class ShoppingListItem(
    @field:JvmField val isBought: Boolean = false, // Need that part or toObject won't correctly transform to Boolean
    var name: String = "",
    var quantity: Int = 0,
    var unit: String = "",
    var documentId: String = ""
)