package com.householdshopper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.householdshopper.model.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import javax.inject.Inject
@HiltViewModel
class AppNavHostViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel(){
    fun checkInHousehold(userID: String):Deferred<Boolean>{
        return viewModelScope.async{
            try {
                val result = userRepository.getUser(userID).householdID
                result != ""
            }catch (e:Exception){
                println(e.message)
                false
            }
        }
    }
}