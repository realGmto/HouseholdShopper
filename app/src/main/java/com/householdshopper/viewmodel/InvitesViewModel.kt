package com.householdshopper.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.householdshopper.model.Invite
import com.householdshopper.model.User
import com.householdshopper.model.repository.FirebaseMessageRepository
import com.householdshopper.model.repository.HouseholdRepository
import com.householdshopper.model.repository.InviteRepository
import com.householdshopper.model.repository.SharedDataRepository
import com.householdshopper.model.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvitesViewModel @Inject constructor(
    private val inviteRepository: InviteRepository,
    private val userRepository: UserRepository,
    private val householdRepository: HouseholdRepository,
    private val sharedDataRepository: SharedDataRepository,
    private val firebaseMessageRepository: FirebaseMessageRepository
): ViewModel(),InviteViewModel {
    private val _selectedItem = MutableStateFlow(0)
    val selectedItem: StateFlow<Int> = _selectedItem

    private val _invites = MutableStateFlow<List<Invite>>(emptyList())
    val invites: StateFlow<List<Invite>> = _invites

    private val _users = MutableStateFlow<List<User>>(emptyList())

    private val user = sharedDataRepository.user.value

    private var sendInvites : List<Invite> = emptyList()
    private var receivedInvites : List<Invite> = emptyList()


    init {
        startListeningSendInvites()
        startListeningReceivedInvites()
        startListeningUsers()
    }
    private fun startListeningSendInvites(){
        viewModelScope.launch {
            inviteRepository.getSendInvitesUpdates(userID = user.documentId).collect{
                sendInvites = it
            }
        }
    }

    private fun startListeningReceivedInvites(){
        viewModelScope.launch {
            inviteRepository.getReceivedInvitesUpdates(userID = user.documentId).collect{
                receivedInvites = it
            }
        }
    }

    private fun startListeningUsers(){
        viewModelScope.launch {
            userRepository.getAllUsersUpdates().collect{
                _users.value = it
                updateInvites(_selectedItem.value)  // Must be called here or it won't load initially
            }
        }
    }

    override fun getUser(userID: String): User{
        return _users.value.first{user ->
            user.documentId == userID
        }
    }

    fun updateInvites(choice: Int){
        _selectedItem.value = choice

        if (choice == 0)
            _invites.value = sendInvites
        else if (choice == 1)
            _invites.value = receivedInvites
    }

    override fun acceptRequest(invite: Invite, context: Context, navHostController: NavHostController){
        viewModelScope.launch {
            val sender: User = getUser(invite.from)

            val title = "Invite"
            val body = "Your invite to the ${sender.username}, has been accepted."

            firebaseMessageRepository.sendNotification(userId = sender.documentId, title = title, body = body, context = context)
            userRepository.updateHousehold(userID = invite.to, householdID = sender.householdID)
            inviteRepository.deleteInvite(invite.documentId)

            val user = userRepository.getUser(FirebaseAuth.getInstance().currentUser?.uid ?: "")
            val household = householdRepository.getSpecificHousehold(user.householdID)

            sharedDataRepository.setHousehold(household)

            navHostController.navigate("home")
        }
    }

    override fun declineRequest(invite: Invite, context: Context){
        viewModelScope.launch {
            val sender: User = getUser(invite.from)

            val title = "Invite"
            val body = "Your invite to the ${sender.username}, has been declined."

            firebaseMessageRepository.sendNotification(userId = sender.documentId, title = title, body = body, context = context)
            inviteRepository.deleteInvite(invite.documentId)
        }
    }
}