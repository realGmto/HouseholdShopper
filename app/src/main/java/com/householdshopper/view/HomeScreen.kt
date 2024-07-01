package com.householdshopper.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.householdshopper.view.items.ShoppingListItem
import com.householdshopper.viewmodel.HomeViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.householdshopper.R
import com.householdshopper.view.components.TopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel) {

    val shoppingLists by viewModel.shoppingLists.collectAsState()
    val household by viewModel.household.collectAsState()

    val resultMessage by viewModel.resultMessage.collectAsState()
    val context = LocalContext.current

    resultMessage.let {
        if (!it.success){
            Toast.makeText(context, it.message,Toast.LENGTH_LONG).show()
        }
    }

    val selectedItem by viewModel.selectedItem.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(title = household.name, navController = navController)
        Box(modifier = Modifier
            .fillMaxSize()
            .weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 25.dp, horizontal = 10.dp)
            ) {
                items(
                    items = shoppingLists,
                    itemContent = {
                        ShoppingListItem(shoppingList = it, viewModel = viewModel, navController = navController)
                    }
                )
            }
            FloatingActionButton(
                onClick = { navController.navigate("createList") },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "new list",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(text = "NEW LIST",
                        modifier = Modifier.padding(end = 4.dp))
                }
            }
        }
        NavigationBar {
            NavigationBarItem(
                icon = { Icon(painter = painterResource(id = R.drawable.outline_shopping_bag_24), contentDescription = "Active lists") },
                label = { Text("Active lists") },
                selected = selectedItem == 0,
                onClick = { viewModel.updateLists(0) }
            )
            NavigationBarItem(
                icon = { Icon(painter = painterResource(id = R.drawable.outline_shopping_cart_24), contentDescription = "All lists") },
                label = { Text("All lists") },
                selected = selectedItem == 1,
                onClick = { viewModel.updateLists(1) }
            )
        }
    }
}