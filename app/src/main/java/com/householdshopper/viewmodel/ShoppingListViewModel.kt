package com.householdshopper.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.householdshopper.model.ShoppingList
import com.householdshopper.model.repository.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val repository: ShoppingListRepository): ViewModel() {

    private val _shoppingList = MutableStateFlow<ShoppingList?>(null)
    val shoppingList: StateFlow<ShoppingList?> = _shoppingList

    fun getSpecificShoppingList( listId: String){
        viewModelScope.launch {
            val result = repository.getSpecificShoppingList(listId)
            _shoppingList.value = result
        }
    }
}