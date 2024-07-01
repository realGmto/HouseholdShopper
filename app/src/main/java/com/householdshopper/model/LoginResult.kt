package com.householdshopper.model

data class LoginResult(
    val success: Boolean,
    val inHousehold: Boolean,
    val message: String
)