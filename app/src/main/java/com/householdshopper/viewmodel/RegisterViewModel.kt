package com.householdshopper.viewmodel

import androidx.lifecycle.ViewModel
import com.householdshopper.model.repository.RegisterRepository
import com.householdshopper.model.RegisterResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RegisterViewModel(private val repository: RegisterRepository) : ViewModel() {
    private val _registerResult = MutableStateFlow<RegisterResult?>(null)
    val registerResult: StateFlow<RegisterResult?> = _registerResult


}