package com.householdshopper.model

data class Invite (
    var from: String ="",
    var to: String = "",
    var documentId: String = ""
)

enum class InviteStatus {
    NOT_INVITED,
    INVITED
}