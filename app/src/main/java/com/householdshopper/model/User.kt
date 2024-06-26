package com.householdshopper.model

import android.provider.ContactsContract.CommonDataKinds.Email
import com.google.firebase.firestore.DocumentId

data class User(
    val householdID: String = "",
    val username: String = "",
    var documentId: String = "",
    val token: String = ""
)