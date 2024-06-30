package com.householdshopper.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
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
import com.householdshopper.view.components.TopBar
import com.householdshopper.view.items.ReceiveInviteItem
import com.householdshopper.view.items.SendInviteItem
import com.householdshopper.viewmodel.InvitesViewModel

@Composable
fun InvitesScreen(
    navController: NavHostController,
    viewModel: InvitesViewModel){

    val selectedItem by viewModel.selectedItem.collectAsState()
    val invites by viewModel.invites.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TopBar(title = "Invites", navController = navController)
        Box(modifier = Modifier
            .fillMaxSize()
            .weight(1f)
        ){
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 25.dp, horizontal = 10.dp)
            ) {
                items(
                    items = invites,
                    itemContent = {
                        if (selectedItem == 0){

                            SendInviteItem(invite = it, viewModel = viewModel)
                        }
                        else{
                            ReceiveInviteItem(invite = it, viewModel = viewModel)
                        }
                    }
                )
            }


        }
        NavigationBar {
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Favorite, contentDescription = "Send invites") },
                label = { Text("Send") },
                selected = selectedItem == 0,
                onClick = { viewModel.updateInvites(0) }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Favorite, contentDescription = "Received invites") },
                label = { Text("Received") },
                selected = selectedItem == 1,
                onClick = { viewModel.updateInvites(1) }
            )
        }
    }
}