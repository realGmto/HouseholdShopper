package com.householdshopper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.householdshopper.model.HouseholdResult
import com.householdshopper.model.repository.HouseholdRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateHouseholdViewModel @Inject constructor(
    private val repository: HouseholdRepository
): ViewModel(){
    private val _householdResult = MutableStateFlow<HouseholdResult?>(null)
    val registerResult: StateFlow<HouseholdResult?> = _householdResult

    fun createHousehold(name: String){
        viewModelScope.launch {
            val households = repository.getAllHouseholds()

            if(name.length <= 3){
                _householdResult.value = HouseholdResult(success = false, message = "Name is too short")
            }
            else if (name.length > 20)
                _householdResult.value = HouseholdResult(success = false, message = "Name is too long")
            else if (households.find{ household -> household.name == name } != null)
                _householdResult.value = HouseholdResult(success = false, message = "Household with that name already exists")
            else{
                val result = repository.addNewHousehold(name = name)
                _householdResult.value = result
            }
        }
    }
}