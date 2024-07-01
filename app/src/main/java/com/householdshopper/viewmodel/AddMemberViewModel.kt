package com.householdshopper.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.householdshopper.model.Invite
import com.householdshopper.model.InviteStatus
import com.householdshopper.model.User
import com.householdshopper.model.repository.FirebaseMessageRepository
import com.householdshopper.model.repository.InviteRepository
import com.householdshopper.model.repository.SharedDataRepository
import com.householdshopper.model.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMemberViewModel @Inject constructor(
    private val userRepository: UserRepository,
    sharedDataRepository: SharedDataRepository,
    private val firebaseMessageRepository: FirebaseMessageRepository,
    private val inviteRepository: InviteRepository
): ViewModel() {
    val household = sharedDataRepository.household
    private val currentUser = sharedDataRepository.user

    private val invites = MutableStateFlow(listOf<Invite>())

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _users = MutableStateFlow(listOf<User>())
    val users = searchText
        .onEach { _isSearching.update { true } }
        .combine(_users){ text, users ->
            if(text.isBlank()){
                users
            }else{
                users.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _users.value
        )

    init {
        startListeningUsers()
        startListeningSendInvites()
    }

    private fun startListeningUsers(){
        viewModelScope.launch{
            userRepository.getAllUsersUpdates().collect{
                _users.value = it.filter { user ->
                    user.householdID != household.value.householdId
                }
            }
        }
    }

    private fun startListeningSendInvites(){
        viewModelScope.launch {
            inviteRepository.getSendInvitesUpdates(userID = currentUser.value.documentId).collect{
                invites.value = it
            }
        }
    }

    fun getInviteStatus(invitee: User): InviteStatus {
        val currentUserId = currentUser.value.documentId
        return if (invites.value.firstOrNull{ invite -> invite.from == currentUserId && invite.to == invitee.documentId } != null)
            InviteStatus.INVITED
        else
            InviteStatus.NOT_INVITED
    }

    fun inviteUser(context: Context,invitee: User){
        viewModelScope.launch {
            val currentUserId = currentUser.value.documentId
            val result = inviteRepository.createInvite(from = currentUserId, to = invitee.documentId)
            if (!result){
                Toast.makeText(context,"There was an error while trying to send an invite",Toast.LENGTH_LONG).show()
            }
            else
                firebaseMessageRepository.sendNotification(userId = invitee.documentId, title = "Invite", body = "You have been invited to join ${currentUser.value.username} household", context = context)
        }
    }

    fun onSearchTextChange(text: String){
        _searchText.value = text
    }
}