package com.householdshopper.model

import com.google.firebase.firestore.DocumentId

data class ShoppingListItem(
    @field:JvmField val isBought: Boolean = false, // Need that part or toObject won't correctly transform to Boolean
    val name: String = "",
    val quantity: Int = 0,
    val unit: String = "",
    var documentId: String = ""
)