package com.householdshopper.model.repository

import com.householdshopper.model.Household
import com.householdshopper.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedDataRepository @Inject constructor() {
    private val _user = MutableStateFlow<User>(User())
    val user: StateFlow<User> = _user

    private val _household = MutableStateFlow<Household>(Household())
    val household: StateFlow<Household> = _household

    fun setUser(user: User) {
        _user.value = user
    }

    fun setHousehold(household: Household) {
        _household.value = household
    }
}