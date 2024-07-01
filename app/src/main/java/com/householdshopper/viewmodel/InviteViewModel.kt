package com.householdshopper.viewmodel

import android.content.Context
import androidx.navigation.NavHostController
import com.householdshopper.model.Invite
import com.householdshopper.model.User

interface InviteViewModel {
    fun acceptRequest(invite: Invite, context: Context, navHostController: NavHostController)
    fun declineRequest(invite: Invite,context: Context)
    fun getUser(userID: String): User
}