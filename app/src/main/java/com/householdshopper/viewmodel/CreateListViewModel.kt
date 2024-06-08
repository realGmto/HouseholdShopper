package com.householdshopper.viewmodel

import androidx.navigation.NavHostController
import com.householdshopper.model.repository.ShoppingListRepository
import javax.inject.Inject

class CreateListViewModel @Inject constructor(private val repository: ShoppingListRepository){

    fun createShoppingList(name: String, navController: NavHostController){
        // TODO - Validation
        val result = repository.createNewList(name = name)
    }
}