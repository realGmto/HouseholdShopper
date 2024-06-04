package com.householdshopper.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.householdshopper.model.ShoppingList
import com.householdshopper.model.repository.ShoppingListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShoppingListViewModel(private val repository: ShoppingListRepository, savedStateHandle: SavedStateHandle): ViewModel() {
    private val _shoppingList = MutableStateFlow<ShoppingList?>(null)
    val shoppingList: StateFlow<ShoppingList?> = _shoppingList

    private val listId: String = checkNotNull(savedStateHandle["listId"])

    fun getSpecificShoppingList(){
        viewModelScope.launch {
            val result = repository.getSpecificShoppingList(listId)
            _shoppingList.value = result
        }
    }
}