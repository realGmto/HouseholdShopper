package com.householdshopper.view.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.householdshopper.model.Invite
import com.householdshopper.viewmodel.InvitesViewModel

@Composable
fun SendInviteItem(invite: Invite, viewModel: InvitesViewModel){
    val name by remember {
        mutableStateOf(viewModel.getUser(invite.to).username)
    }
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 4.dp,
        shadowElevation = 8.dp
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
        ){
            Text(
                text = "You have send an invite to $name",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}