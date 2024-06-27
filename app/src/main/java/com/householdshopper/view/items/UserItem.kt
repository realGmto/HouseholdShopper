package com.householdshopper.view.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.householdshopper.model.User
import com.householdshopper.viewmodel.HouseholdViewModel

@Composable
fun UserItem(
    user: User,
    viewModel: HouseholdViewModel
){
    Surface (
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 4.dp,
        shadowElevation = 8.dp
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = user.username,
                modifier = Modifier.padding(4.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            if ( viewModel.isOwner(user.documentId)){
                Text(text = "Owner", modifier = Modifier.padding(4.dp))
            }else{
                IconButton(onClick = { viewModel.removeMemberFromHousehold(userID = user.documentId) }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Kick member",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}