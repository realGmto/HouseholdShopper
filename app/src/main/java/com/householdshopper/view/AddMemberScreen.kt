package com.householdshopper.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.householdshopper.view.components.TopBar
import com.householdshopper.view.items.AddUserItem
import com.householdshopper.viewmodel.AddMemberViewModel

@Composable
fun AddMemberScreen(
    viewModel:AddMemberViewModel,
    navController: NavHostController
){
    val household by viewModel.household.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val users by viewModel.users.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TopBar(title = household.name, navController = navController )
        TextField(
            value = searchText,
            onValueChange = viewModel::onSearchTextChange,
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text(text = "Search")}
        )
        Spacer(modifier = Modifier.height(16.dp))
        if(isSearching){
            Box(modifier = Modifier.fillMaxSize()){
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        else{
            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ){
                items(
                    items = users,
                    itemContent = {
                        AddUserItem(user = it,viewModel = viewModel)
                    }
                )
            }
        }
    }
}