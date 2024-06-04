package com.householdshopper.model

import com.google.firebase.firestore.DocumentId

data class ShoppingListItem(
    val isBought: Boolean = false,
    val name: String = "",
    val quantity: Int = 0,
    val unit: String = "",
    var documentId: String = ""
)