package com.householdshopper.viewmodel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.api.Context
import com.householdshopper.model.repository.SharedDataRepository
import com.householdshopper.model.repository.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateListViewModel @Inject constructor(
    private val repository: ShoppingListRepository,
    private val sharedDataRepository: SharedDataRepository
): ViewModel(){

    fun createShoppingList(name: String, navController: NavHostController){
        viewModelScope.launch{
            if (name.length <= 3){
                println("Error") // TODO - error message to user
            }
            else{
                val documentId = repository.createNewList(user = sharedDataRepository.user.value, household = sharedDataRepository.household.value,name = name)
                if (documentId != "")
                    navController.navigate("list/${documentId}"){
                        popUpTo("home")
                    }
            }
        }
    }
}