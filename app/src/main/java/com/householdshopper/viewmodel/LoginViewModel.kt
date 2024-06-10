package com.householdshopper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.householdshopper.model.repository.LoginRepository
import com.householdshopper.model.LoginResult
import com.householdshopper.model.repository.HouseholdRepository
import com.householdshopper.model.repository.SharedDataRepository
import com.householdshopper.model.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val sharedDataRepository: SharedDataRepository,
    private val userRepository: UserRepository,
    private val householdRepository: HouseholdRepository
) : ViewModel() {
    private val _loginResult = MutableStateFlow<LoginResult?>(null)
    val loginResult: StateFlow<LoginResult?> = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = repository.login(email, password)
            if(result.success){
                val user = userRepository.getUser(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                val household = householdRepository.getSpecificHousehold(user.householdID)

                sharedDataRepository.setUser(user)
                sharedDataRepository.setHousehold(household)
            }
            _loginResult.value = result
        }
    }
}