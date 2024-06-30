package com.householdshopper.view.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.householdshopper.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String,navController: NavHostController){
    var isExpanded by remember {
        mutableStateOf(false)
    }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        },
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
                        }
                    },
                    onClick = {
                        isExpanded = false
                        navController.navigate("household")
                    })
                DropdownMenuItem(
                    text = {
                        Row {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_move_to_inbox_24),
                                contentDescription = "Inbox",
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text("Invites")
                        }
                    },
                    onClick = {
                        isExpanded = false
                        navController.navigate("invite")
                    }
                )
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
}