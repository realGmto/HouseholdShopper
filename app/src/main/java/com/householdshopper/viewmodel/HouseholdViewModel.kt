package com.householdshopper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.householdshopper.model.User
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
    private val userRepository: UserRepository
): ViewModel(){
    val household = sharedDataRepository.household

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

    fun removeMemberFromHousehold(userID:String){

    }
}