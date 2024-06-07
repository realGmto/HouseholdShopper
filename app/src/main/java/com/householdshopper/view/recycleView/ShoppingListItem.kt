package com.householdshopper.view.recycleView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.householdshopper.model.ShoppingList
import com.householdshopper.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListItem (shoppingList:ShoppingList, viewModel: HomeViewModel, navController: NavController){
    Card(                   // Have to disable card click on clicking an delete icon
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(corner = CornerSize(25)),
        onClick = { navController.navigate("list/${shoppingList.documentId}") },
        colors = CardDefaults.cardColors(
            containerColor = viewModel.getCardBackground(shoppingList)
        )
    ) {
        Row (
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column {
                Text(
                    text = shoppingList.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 22.sp
                )
                Row {
                    //TODO implement progress bar
                    Text(
                        text = "${viewModel.countRemainingItems(shoppingList.items)}/${shoppingList.items.size}",
                        fontWeight = FontWeight.Medium,
                        fontSize = 22.sp
                    )
                }
            }
            Icon(
                Icons.Outlined.Close,
                contentDescription = "Delete list"
            )
        }
    }
}