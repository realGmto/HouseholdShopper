package com.householdshopper.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.householdshopper.view.components.RestrictedTopBar
import com.householdshopper.view.items.ReceiveInviteItem
import com.householdshopper.viewmodel.RestrictedInvitesViewModel

@Composable
fun RestrictedInvitesScreen(
    navController: NavHostController,
    viewModel: RestrictedInvitesViewModel,
){
    val invites by viewModel.invites.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        RestrictedTopBar(title = "Invites", navController = navController)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(vertical = 25.dp, horizontal = 10.dp)
        ) {
            items(
                items = invites,
                itemContent = {
                    ReceiveInviteItem(invite = it, viewModel = viewModel)
                }
            )
        }
    }
}