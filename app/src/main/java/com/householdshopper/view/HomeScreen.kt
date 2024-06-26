package com.householdshopper.view

import android.os.Build
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.householdshopper.view.recycleView.ShoppingListItem
import com.householdshopper.viewmodel.HomeViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.coroutineScope


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel) {

    val shoppingLists by viewModel.shoppingLists.collectAsState()
    val household by viewModel.household.collectAsState()

    val selectedItem by viewModel.selectedItem.collectAsState()

    val context = LocalContext.current

    var isExpanded by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        viewModel.sendMessage(context)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                text = household.name,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )},
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go back"
                    )
                }
            },
            actions = {
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu"
                    )
                }

                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Row {
                                Icon(
                                    imageVector = Icons.Default.AccountBox,
                                    contentDescription = "Household",
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text("Household")
                            }},
                        onClick = {
                            isExpanded = false
                            navController.navigate("household")
                        })
                    DropdownMenuItem(
                        text = {
                            Row {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                    contentDescription = "Logout",
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text("Logout")
                            }
                        },
                        onClick = {
                            FirebaseAuth.getInstance().signOut()
                            isExpanded = false
                            navController.navigate("login")
                        }
                    )
                }
            }
        )
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
                icon = { Icon(Icons.Filled.Favorite, contentDescription = "Active lists") },
                label = { Text("Active lists") },
                selected = selectedItem == 0,
                onClick = { viewModel.updateLists(0) }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Favorite, contentDescription = "All lists") },
                label = { Text("All lists") },
                selected = selectedItem == 1,
                onClick = { viewModel.updateLists(1) }
            )
        }
    }
}