package com.householdshopper.model

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.compose.ui.text.toLowerCase
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Query

data class User(
    val householdID: String = "",
    val username: String = "",
    var documentId: String = "",
    val token: String = ""
){
    fun doesMatchSearchQuery(query: String):Boolean{
        // This is useful if you want to have multiple rules while searching
        val matchingCombination = listOf(
            username
        )

        return matchingCombination.any{
            it.contains(query, ignoreCase = true)
        }
    }
}