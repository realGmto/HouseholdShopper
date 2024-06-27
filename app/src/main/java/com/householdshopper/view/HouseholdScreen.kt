package com.householdshopper.view


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.householdshopper.view.components.TopBar
import com.householdshopper.view.items.UserItem
import com.householdshopper.viewmodel.HouseholdViewModel

@Composable
fun HouseholdScreen(
    navController: NavHostController,
    viewModel: HouseholdViewModel
){
    val household by viewModel.household.collectAsState()
    val users by viewModel.users.collectAsState()
    val resultMessage by viewModel.resultMessage.collectAsState()

    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        TopBar(title = household.name, navController = navController)

        Box(modifier = Modifier
            .fillMaxSize()
            .weight(1f)
        ){
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 25.dp, horizontal = 10.dp)
            ){
                items(
                    items = users,
                    itemContent = {
                        UserItem(user = it, viewModel = viewModel)
                    }
                )
            }
            resultMessage.let {
                if(it.success){
                    Text(
                        text = it.message,
                        color = Color.Green
                    )
                }else{
                    Text(
                        text = it.message,
                        color = Color.Red
                    )
                }
            }
            FloatingActionButton(
                onClick = { /* TODO - navigation to adding new user*/ },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
                    .offset(y = (-100).dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add new user",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(text = "ADD USER",
                        modifier = Modifier.padding(end = 4.dp))
                }
            }
        }
    }
}