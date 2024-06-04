package com.householdshopper.model

import android.provider.ContactsContract.CommonDataKinds.Email

data class User(
    val username: String,
    val householdID: String
)