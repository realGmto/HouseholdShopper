package com.householdshopper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.householdshopper.model.repository.RegisterRepository
import com.householdshopper.model.RegisterResult
import com.householdshopper.model.User
import com.householdshopper.model.repository.SharedDataRepository
import com.householdshopper.model.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: RegisterRepository,
    private val userRepository: UserRepository,
    private val sharedDataRepository: SharedDataRepository
) : ViewModel() {

    private val _registerResult = MutableStateFlow<RegisterResult?>(null)
    val registerResult: StateFlow<RegisterResult?> = _registerResult

    fun register(
        email: String,
        password: String,
        repeatPassword: String,
        username: String,
        householdId: String) {

        var valid = true
        viewModelScope.launch {
            var users : List<User> = userRepository.getAllUsers()
            var result : RegisterResult = RegisterResult(success = true, message = "")

            if (users.find{ user -> user.username == username } != null){
                valid = false
                result = RegisterResult(success = false, message = "Username is already in use")
            }
            else if (username.length < 3){
                valid = false
                result = RegisterResult(success = false, message = "Username is too short")
            }
            else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                valid = false
                result = RegisterResult(success = false, message = "Email is not valid")
            }
            else if (repeatPassword != password){
                valid = false
                result = RegisterResult(success = false, message = "Passwords doesn't match")
            }
            if (valid){
                result = repository.register(email,password,username,householdId)
                if (result.success){
                    val user = userRepository.getUser(FirebaseAuth.getInstance().currentUser?.uid ?: "")

                    sharedDataRepository.setUser(user)
                }
            }
            _registerResult.value = result
        }
    }


}