package com.householdshopper.view

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.householdshopper.model.Household
import com.householdshopper.ui.theme.gray
import com.householdshopper.ui.theme.light_gray
import com.householdshopper.view.recycleView.UserItem
import com.householdshopper.viewmodel.HouseholdViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseholdScreen(
    navController: NavHostController,
    viewModel: HouseholdViewModel
){
    val household by viewModel.household.collectAsState()
    val users by viewModel.users.collectAsState()

    var isExpanded by remember {
        mutableStateOf(false)
    }

    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        CenterAlignedTopAppBar(
            modifier = Modifier.background(light_gray),
            title ={
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
                ){
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