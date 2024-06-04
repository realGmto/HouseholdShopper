package com.householdshopper.view.recycleView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.householdshopper.model.ShoppingListItem
import com.householdshopper.ui.theme.light_gray
import com.householdshopper.viewmodel.ShoppingListViewModel

@Composable
fun ShoppingListItemsItem(item: ShoppingListItem, viewModel: ShoppingListViewModel){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background(light_gray)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "isBought"
            )
        }
        Text(
            text = item.name
        )
        Text(
            text = "${item.quantity} ${item.unit}"
        )
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Filled.Create,
                contentDescription = "Update"
            )
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete"
            )
        }
    }
}