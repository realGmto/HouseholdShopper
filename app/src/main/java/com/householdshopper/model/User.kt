package com.householdshopper.model

import android.provider.ContactsContract.CommonDataKinds.Email

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val password: String
)