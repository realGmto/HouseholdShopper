package com.householdshopper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.householdshopper.model.ShoppingList
import com.householdshopper.model.repository.ShoppingListItemsRepository
import com.householdshopper.model.repository.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
    private val itemsRepository: ShoppingListItemsRepository
): ViewModel() {
    private val _shoppingList = MutableStateFlow<ShoppingList?>(null)
    val shoppingList: StateFlow<ShoppingList?> = _shoppingList

    fun getSpecificShoppingList( listId: String){
        viewModelScope.launch {
            val result = shoppingListRepository.getSpecificShoppingList(listId)
            _shoppingList.value = result
        }
    }

    fun addNewItem(listId: String,name: String,quantity:Int,unit:String){
        viewModelScope.launch {
            val result = itemsRepository.addItemToList(listId = listId, name = name, quantity = quantity, unit = unit)
            // TODO - notify users that new item is added
            if (result)
                getSpecificShoppingList(listId)
        }
    }

    fun updateItem(){

    }

    fun removeItem(){

    }
}