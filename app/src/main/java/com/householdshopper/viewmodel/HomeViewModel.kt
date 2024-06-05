package com.householdshopper.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.householdshopper.model.ShoppingList
import com.householdshopper.model.ShoppingListItem
import com.householdshopper.model.repository.ShoppingListRepository
import com.householdshopper.model.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(private val shoppingListRepository: ShoppingListRepository, private val userRepository: UserRepository): ViewModel() {
    private val _shoppingLists = MutableStateFlow<List<ShoppingList>>(emptyList())
    val shoppingLists: StateFlow<List<ShoppingList>> = _shoppingLists

    fun getActiveLists(){
        viewModelScope.launch{
            val householdID = userRepository.getUser(FirebaseAuth.getInstance().currentUser?.uid ?: "")?.householdID?: ""
            val result = shoppingListRepository.getActiveShoppingLists(householdID)
            _shoppingLists.value = result
        }
    }

    fun getAllLists(){
        viewModelScope.launch {
            val householdID = userRepository.getUser(FirebaseAuth.getInstance().currentUser?.uid ?: "")?.householdID?: ""
            val result =shoppingListRepository.getAllShoppingLists(householdID)
            _shoppingLists.value = result
        }
    }

    fun updateScreen(index:Int){
        if (index == 0){
            getActiveLists()
        }else if (index ==1){
            getAllLists()
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