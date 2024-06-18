package com.householdshopper.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentId
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

    fun validateItemData(context: Context,name: String,quantity:Int,unit:String):Boolean{
        var valid = true
        if (name.length <= 3){
            valid = false
            Toast.makeText(context,"Name is too short. Try again",Toast.LENGTH_LONG).show()
        }
        if (quantity <= 0){
            valid = false
            Toast.makeText(context,"Quantity can't be non positive number. Try again",Toast.LENGTH_LONG).show()
        }
        if (unit == "" || unit.contains("[0-9]".toRegex())){
            valid = false
            Toast.makeText(context,"Unit can't be empty or contain number. Try again",Toast.LENGTH_LONG).show()
        }
        if (valid)
            addNewItem(name,quantity,unit)
        return valid
    }

    fun addNewItem(name: String,quantity:Int,unit:String){
        viewModelScope.launch {
            val result = itemsRepository.addItemToList(listId = _shoppingList.value?.documentId ?: "", name = name, quantity = quantity, unit = unit)
            // TODO - notify users that new item is added
            if (result)
                getSpecificShoppingList(_shoppingList.value?.documentId ?: "")
        }
    }

    fun updateItem(){

    }

    fun removeItem(documentId: String){
        viewModelScope.launch{
            val result = itemsRepository.removeItemFromList(listId = _shoppingList.value?.documentId ?: "", documentId = documentId)

            if (result)
                getSpecificShoppingList(_shoppingList.value?.documentId ?: "")
        }
    }

    fun updateBoughtStatus(documentId: String, state:Boolean){
        viewModelScope.launch{
            itemsRepository.updateIsBought(listId = _shoppingList.value?.documentId ?: "", documentId = documentId, state = state)
        }
    }
}