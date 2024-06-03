package com.householdshopper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.householdshopper.model.ShoppingList
import com.householdshopper.model.ShoppingListItem
import com.householdshopper.model.repository.ShoppingListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ShoppingListRepository): ViewModel() {
    private val _shoppingLists = MutableStateFlow<List<ShoppingList>>(emptyList())
    val shoppingLists: StateFlow<List<ShoppingList>> = _shoppingLists

    fun getActiveLists(householdID: String){
        viewModelScope.launch{
            val result = repository.getActiveShoppingLists(householdID)
            _shoppingLists.value = result
        }
    }

    fun getAllLists(householdID: String){
        viewModelScope.launch {
            val result =repository.getAllShoppingLists(householdID)
            _shoppingLists.value = result
        }
    }

    fun updateScreen(index:Int,householdID: String){
        if (index == 0){
            getActiveLists(householdID)
        }else if (index ==1){
            getAllLists(householdID)
        }/*TODO - implement function to get all members of household if index is 2*/
    }

    fun countRemainingItems(items: List<ShoppingListItem>):Int{
        var counter = 0
        items.forEach{item ->
            if (item.isBought)
                counter++
        }
        return counter
    }
}