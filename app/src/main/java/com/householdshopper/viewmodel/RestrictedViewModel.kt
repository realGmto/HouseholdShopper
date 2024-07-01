package com.householdshopper.viewmodel

import androidx.lifecycle.ViewModel
import com.householdshopper.model.repository.FirebaseMessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RestrictedViewModel @Inject constructor(
    firebaseMessageRepository: FirebaseMessageRepository
): ViewModel() {
    init {
        firebaseMessageRepository.getTokenAndSendToServer()
    }
}