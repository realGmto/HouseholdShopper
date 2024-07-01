package com.householdshopper.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.householdshopper.model.Invite
import com.householdshopper.model.User
import com.householdshopper.model.repository.FirebaseMessageRepository
import com.householdshopper.model.repository.InviteRepository
import com.householdshopper.model.repository.SharedDataRepository
import com.householdshopper.model.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestrictedInvitesViewModel @Inject constructor(
    private val inviteRepository: InviteRepository,
    private val userRepository: UserRepository,
    private val firebaseMessageRepository: FirebaseMessageRepository,
    sharedDataRepository: SharedDataRepository
): ViewModel(), InviteViewModel {
    private val _invites = MutableStateFlow<List<Invite>>(emptyList())
    val invites: StateFlow<List<Invite>> = _invites

    private val _users = MutableStateFlow<List<User>>(emptyList())

    private val user = sharedDataRepository.user.value

    init {
        startListeningReceivedInvites()
        startListeningUsers()
    }

    private fun startListeningReceivedInvites(){
        viewModelScope.launch {
            inviteRepository.getReceivedInvitesUpdates(userID = user.documentId).collect{
                _invites.value = it
            }
        }
    }

    private fun startListeningUsers(){
        viewModelScope.launch {
            userRepository.getAllUsersUpdates().collect{
                _users.value = it
            }
        }
    }

    override fun acceptRequest(invite: Invite, context: Context, navHostController: NavHostController) {
        viewModelScope.launch {
            val sender: User = getUser(invite.from)

            val title = "Invite"
            val body = "Your invite to the ${sender.username}, has been accepted."

            firebaseMessageRepository.sendNotification(userId = sender.documentId, title = title, body = body, context = context)
            userRepository.updateHousehold(userID = invite.to, householdID = sender.householdID)
            inviteRepository.deleteInvite(invite.documentId)
            navHostController.navigate("home")
        }
    }

    override fun declineRequest(invite: Invite, context: Context) {
        viewModelScope.launch {
            val sender: User = getUser(invite.from)

            val title = "Invite"
            val body = "Your invite to the ${sender.username}, has been declined."

            firebaseMessageRepository.sendNotification(userId = sender.documentId, title = title, body = body, context = context)
            inviteRepository.deleteInvite(invite.documentId)
        }
    }

    override fun getUser(userID: String): User {
        return _users.value.first{user ->
            user.documentId == userID
        }
    }
}