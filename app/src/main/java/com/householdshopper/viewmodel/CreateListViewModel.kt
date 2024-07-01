package com.householdshopper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.householdshopper.model.ResultMessage
import com.householdshopper.model.repository.SharedDataRepository
import com.householdshopper.model.repository.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateListViewModel @Inject constructor(
    private val repository: ShoppingListRepository,
    private val sharedDataRepository: SharedDataRepository
): ViewModel(){
    private val _resultMessage = MutableStateFlow(ResultMessage())
    val resultMessage = _resultMessage.asStateFlow()

    fun createShoppingList(name: String, navController: NavHostController){
        viewModelScope.launch{
            if (name.length <= 3){
                _resultMessage.value = ResultMessage(success = false,"Name is too short")
            }
            else{
                val documentId = repository.createNewList(user = sharedDataRepository.user.value, household = sharedDataRepository.household.value,name = name)
                if (documentId != ""){
                    _resultMessage.value = ResultMessage(success = true,"Successfully created shopping list")
                    navController.navigate("list/${documentId}"){
                        popUpTo("home")
                    }
                }
                else
                    _resultMessage.value = ResultMessage(success = false,"There was an error while trying to create shopping list")
            }
        }
    }
}