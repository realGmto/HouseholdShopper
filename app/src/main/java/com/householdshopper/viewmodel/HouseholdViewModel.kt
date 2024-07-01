package com.householdshopper.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.householdshopper.model.ResultMessage
import com.householdshopper.model.User
import com.householdshopper.model.repository.FirebaseMessageRepository
import com.householdshopper.model.repository.HouseholdRepository
import com.householdshopper.model.repository.SharedDataRepository
import com.householdshopper.model.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HouseholdViewModel @Inject constructor(
    private val sharedDataRepository: SharedDataRepository,
    private val householdRepository: HouseholdRepository,
    private val firebaseMessageRepository: FirebaseMessageRepository,
    private val userRepository: UserRepository
): ViewModel(){
    val household = sharedDataRepository.household

    private val _resultMessage = MutableStateFlow(ResultMessage())
    val resultMessage: StateFlow<ResultMessage> = _resultMessage

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    init {
        startListeningUsers()
    }

    private fun startListeningUsers(){
        viewModelScope.launch{
            userRepository.getSpecificUsersUpdates(householdID = household.value.householdId).collect{
                _users.value = it
            }
        }
    }

    fun removeMemberFromHousehold(userID:String, context: Context){
        viewModelScope.launch {
            val result = householdRepository.removeUserFromHousehold(userID = userID,household.value.householdId)
            _resultMessage.value = result
            if (result.success)
                firebaseMessageRepository.sendNotification(userId = userID,"Household","You have been kicked from the household by ${sharedDataRepository.user.value.username}", context = context)
        }
    }

    fun isOwner(userID: String): Boolean {
        return household.value.creator == userID
    }
}