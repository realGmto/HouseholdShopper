package com.householdshopper.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.householdshopper.R
import com.householdshopper.ui.theme.gray
import com.householdshopper.ui.theme.light_gray
import com.householdshopper.ui.theme.modal_background
import com.householdshopper.view.items.ShoppingListItemsItem
import com.householdshopper.viewmodel.ShoppingListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    navController: NavHostController,
    viewModel: ShoppingListViewModel,
    listId : String
){
    val shoppingList by viewModel.shoppingList.collectAsState()
    val context = LocalContext.current

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableIntStateOf(1) }
    var unit by remember { mutableStateOf("") }

    // TODO - Implement validation
    var nameError by remember { mutableStateOf(false) }
    var quantityError by remember { mutableStateOf(false) }
    var unitError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadInitialData(listId)
    }

    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        CenterAlignedTopAppBar(
            modifier = Modifier.background(gray),
            title = {
                Text(
                    text = "${shoppingList?.name}",
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
                IconButton(onClick = { /* TODO - assign user */ }) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Add person"
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
                    items = shoppingList?.items ?: emptyList(),
                    itemContent = {
                        ShoppingListItemsItem(item = it, viewModel = viewModel)
                    }
                )
            }
            FloatingActionButton(
                onClick = { showBottomSheet = true },
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
                        contentDescription = "Add Item",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = "ADD ITEM",
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
            }
        }
        if (showBottomSheet){
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                Column (
                    verticalArrangement = Arrangement.Center
                ){
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        IconButton(onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                /* TODO- call function to add item to shopping list */
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Go back"
                            )
                        }

                        OutlinedTextField(
                            value = name,
                            onValueChange = {
                                name = it
                                nameError = false},
                            placeholder = { Text(text = "Product name", fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)) },
                            shape = RoundedCornerShape(25),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = light_gray.copy(alpha = 0.5f),
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                unfocusedContainerColor = light_gray,
                                focusedContainerColor = light_gray,
                            ),
                            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onBackground),
                            isError = nameError,
                            modifier = Modifier
                                .background(
                                    modal_background,
                                    RoundedCornerShape(25)
                                )
                                .width(300.dp)
                                .padding(8.dp)
                        )
                    }
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        OutlinedTextField(
                            value = quantity.toString(),
                            onValueChange = {quantity = it.toInt()
                                quantityError = false},
                            label = { Text(text = "Quantity", fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)) },
                            shape = RoundedCornerShape(25),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = light_gray.copy(alpha = 0.5f),
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                unfocusedContainerColor = light_gray,
                                focusedContainerColor = light_gray,
                            ),
                            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onBackground),
                            isError = quantityError,
                            modifier = Modifier
                                .background(
                                    modal_background,
                                    RoundedCornerShape(25)
                                )
                                .width(125.dp)
                                .padding(8.dp)
                        )

                        OutlinedTextField(
                            value = unit,
                            onValueChange = {
                                unit = it
                                unitError = false},
                            label = { Text(text = "Unit", fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)) },
                            shape = RoundedCornerShape(25),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = light_gray.copy(alpha = 0.5f),
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                unfocusedContainerColor = light_gray,
                                focusedContainerColor = light_gray,
                            ),
                            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onBackground),
                            isError = unitError,
                            modifier = Modifier
                                .background(
                                    modal_background,
                                    RoundedCornerShape(25)
                                )
                                .width(125.dp)
                                .padding(8.dp)
                        )

                        IconButton(
                            onClick = { quantity += 1 }
                        ){
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(
                            onClick = { quantity += -1 },
                            enabled = quantity != 0
                        ){
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_remove_24),
                                contentDescription = "Sub",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        Text(
                            text = "UNIT",
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(6.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { unit = "kg" },
                            colors = ButtonDefaults.buttonColors(containerColor = light_gray),
                            modifier = Modifier.size(40.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "kg",
                                color = Color.White,
                                fontSize = 10.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { unit = "g" },
                            colors = ButtonDefaults.buttonColors(containerColor = light_gray),
                            modifier = Modifier.size(40.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(text = "g",
                                color = Color.White,
                                fontSize = 10.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { unit = "l" },
                            colors = ButtonDefaults.buttonColors(containerColor = light_gray),
                            modifier = Modifier.size(40.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(text = "l",
                                color = Color.White,
                                fontSize = 10.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { unit = "ml" },
                            colors = ButtonDefaults.buttonColors(containerColor = light_gray),
                            modifier = Modifier.size(40.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(text = "ml",
                                color = Color.White,
                                fontSize = 10.sp)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Button(onClick = {
                            scope.launch {
                                val result = viewModel.validateItemData(context, name, quantity, unit)
                                if (sheetState.isVisible && result) {
                                    showBottomSheet = false
                                    sheetState.hide()
                                }
                            }
                        }) {
                            Text("Save")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}
