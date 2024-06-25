package com.householdshopper.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.householdshopper.ui.theme.gray
import com.householdshopper.ui.theme.light_gray
import com.householdshopper.viewmodel.CreateHouseholdViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHouseholdScreen(
    navController: NavHostController,
    viewModel: CreateHouseholdViewModel
){
    var name by remember {
        mutableStateOf("")
    }
    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        CenterAlignedTopAppBar(
            modifier = Modifier.background(gray),
            title = { Text("Create Household") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go back"
                    )
                }
            }
        )

        OutlinedTextField(
            value = name,
            onValueChange = {name = it},
            placeholder = { Text(text = "Name", fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)) },
            shape = RoundedCornerShape(25),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = light_gray.copy(alpha = 0.5f),
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = light_gray,
                focusedContainerColor = light_gray,
            ),
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(25))
                .width(300.dp)
                .padding(8.dp)
        )

        Button(
            onClick = { viewModel.createHousehold(name = name) },
            colors = ButtonDefaults.buttonColors(containerColor = light_gray)
        ) {
            Text(
                text = "Create",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

    }
}