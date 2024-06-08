package com.householdshopper.model

data class Household (
    val members: List<String> = emptyList(),
    val name: String = "",
    var householdId: String = ""
)