package com.householdshopper.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.householdshopper.R
import com.householdshopper.view.components.CardBox
import com.householdshopper.viewmodel.RestrictedViewModel

@Composable
fun RestrictedScreen(
    navController: NavHostController,
    viewModel: RestrictedViewModel
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardBox(
            icon = painterResource(id = R.drawable.baseline_add_home_24),
            title = "Create Household",
            description = "Create a new household.",
            onClick = { navController.navigate("createHousehold") }
        )
        CardBox(
            icon = painterResource(id = R.drawable.baseline_person_add_24),
            title = "Invites to Household",
            description = "View and manage household invites.",
            onClick = { navController.navigate("invitesToHousehold") }
        )
    }
}