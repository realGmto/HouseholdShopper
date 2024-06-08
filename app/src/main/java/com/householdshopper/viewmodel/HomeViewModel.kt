package com.householdshopper.viewmodel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.householdshopper.model.Household
import com.householdshopper.model.ShoppingList
import com.householdshopper.model.ShoppingListItem
import com.householdshopper.model.repository.HouseholdRepository
import com.householdshopper.model.repository.ShoppingListRepository
import com.householdshopper.model.repository.UserRepository
import com.householdshopper.ui.theme.green
import com.householdshopper.ui.theme.light_gray
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(private val shoppingListRepository: ShoppingListRepository, private val userRepository: UserRepository, private val householdRepository: HouseholdRepository): ViewModel() {
    private val _shoppingLists = MutableStateFlow<List<ShoppingList>>(emptyList())
    val shoppingLists: StateFlow<List<ShoppingList>> = _shoppingLists

    private val _household = MutableStateFlow(Household())
    val household: StateFlow<Household> = _household

    private val _selectedItem = MutableStateFlow(0)
    val selectedItem: StateFlow<Int> = _selectedItem

    init {
        getHousehold()
        getActiveLists()
    }

    fun getHousehold(){
        viewModelScope.launch {
            val householdID = userRepository.getUser(FirebaseAuth.getInstance().currentUser?.uid ?: "")?.householdID?: ""
            val result = householdRepository.getSpecificHousehold(householdID)
            _household.value = result
        }
    }

    fun getActiveLists(){
        viewModelScope.launch{
            val result = shoppingListRepository.getActiveShoppingLists(_household.value.householdId)
            _shoppingLists.value = result
        }
    }

    fun getAllLists(){
        viewModelScope.launch {
            val result =shoppingListRepository.getAllShoppingLists(_household.value.householdId)
            _shoppingLists.value = result
        }
    }

    fun updateLists(choice: Int){
        _selectedItem.value = choice

        if (choice == 0)
            getActiveLists()
        else
            getAllLists()
    }

    fun countRemainingItems(items: List<ShoppingListItem>):Int{
        var counter = 0
        items.forEach{item ->
            Log.e("Counter", item.toString())
            if (item.isBought)
                counter +=1
        }
        return counter
    }

    fun getCardBackground(list: ShoppingList): Color{
        return if (list.isActive)
            light_gray
        else green
    }
}