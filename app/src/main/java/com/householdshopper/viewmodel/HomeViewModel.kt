package com.householdshopper.viewmodel

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.householdshopper.model.ShoppingList
import com.householdshopper.model.ShoppingListItem
import com.householdshopper.model.repository.FirebaseMessageRepository
import com.householdshopper.model.repository.HouseholdRepository
import com.householdshopper.model.repository.SharedDataRepository
import com.householdshopper.model.repository.ShoppingListItemsRepository
import com.householdshopper.model.repository.ShoppingListRepository
import com.householdshopper.model.repository.UserRepository
import com.householdshopper.ui.theme.green
import com.householdshopper.ui.theme.light_gray
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
    private val sharedDataRepository: SharedDataRepository,
    private val userRepository: UserRepository,
    private val householdRepository: HouseholdRepository,
    private val itemsRepository: ShoppingListItemsRepository,
    private val firebaseMessageRepository: FirebaseMessageRepository
): ViewModel() {
    private val _shoppingLists = MutableStateFlow<List<ShoppingList>>(emptyList())
    val shoppingLists: StateFlow<List<ShoppingList>> = _shoppingLists

    private var allShoppingList: List<ShoppingList> = emptyList()

    val household = sharedDataRepository.household

    private val _selectedItem = MutableStateFlow(0)
    val selectedItem: StateFlow<Int> = _selectedItem

    init {
        viewModelScope.launch{
            val user = userRepository.getUser(FirebaseAuth.getInstance().currentUser?.uid ?: "")
            val household = householdRepository.getSpecificHousehold(user.householdID)

            sharedDataRepository.setUser(user)
            sharedDataRepository.setHousehold(household)

            startListeningList()
            firebaseMessageRepository.getTokenAndSendToServer()
        }
    }

    fun sendMessage(context: Context){
        viewModelScope.launch {
            firebaseMessageRepository.sendNotification(userId = FirebaseAuth.getInstance().currentUser?.uid ?: "", title = "Test title", body = "Test body", context =context)
        }
    }


    fun startListeningList() {
        viewModelScope.launch {
            shoppingListRepository.getShoppingListsUpdates(household.value.householdId)
                .flatMapLatest {lists ->
                    val itemFlows = lists.map { list ->
                        itemsRepository.getItemsUpdates(list.documentId).map { items -> list.copy(items = items) }
                    }
                    combine(itemFlows) { updateLists ->
                        updateLists.toList()
                    }
                }
                .collect { updatedLists ->
                    allShoppingList = updatedLists
                    updateLists(_selectedItem.value)
                }
        }
    }

    fun getActiveLists(){
        _shoppingLists.value = allShoppingList.filter { countBoughtItems(it.items) != it.items.size}
    }

    fun getAllLists(){
        _shoppingLists.value = allShoppingList
    }

    fun updateLists(choice: Int){
        _selectedItem.value = choice

        if (choice == 0)
            getActiveLists()
        else
            getAllLists()
    }

    fun countBoughtItems(items: List<ShoppingListItem>):Int{
        var counter = 0
        items.forEach{item ->
            if (item.isBought)
                counter +=1
        }
        return counter
    }

    fun getCardBackground(list: ShoppingList): Color{
        return if (countBoughtItems(list.items) != list.items.size)
            light_gray
        else green
    }
}