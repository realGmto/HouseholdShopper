package com.householdshopper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.householdshopper.model.Invite
import com.householdshopper.model.User
import com.householdshopper.model.repository.InviteRepository
import com.householdshopper.model.repository.SharedDataRepository
import com.householdshopper.model.repository.UserRepository
import com.householdshopper.view.items.SendInviteItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvitesViewModel @Inject constructor(
    private val inviteRepository: InviteRepository,
    private val userRepository: UserRepository,
    private val sharedDataRepository: SharedDataRepository
): ViewModel() {
    private val _selectedItem = MutableStateFlow(0)
    val selectedItem: StateFlow<Int> = _selectedItem

    private val _invites = MutableStateFlow<List<Invite>>(emptyList())
    val invites: StateFlow<List<Invite>> = _invites

    private val _users = MutableStateFlow<List<User>>(emptyList())

    private val user = sharedDataRepository.user.value

    private var sendInvites : List<Invite> = emptyList()
    private var receivedInvites : List<Invite> = emptyList()


    init {
        startListeningInvites()
        startListeningUsers()
        updateInvites(0)
    }
    private fun startListeningInvites(){
        viewModelScope.launch {
            inviteRepository.getSendInvitesUpdates(userID = user.documentId).collect{
                sendInvites = it
            }
            inviteRepository.getReceivedInvitesUpdates(userID = user.documentId).collect{
                receivedInvites = it
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

    fun getUser(userID: String): User{
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
}