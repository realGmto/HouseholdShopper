package com.householdshopper.view.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.householdshopper.model.InviteStatus
import com.householdshopper.model.User
import com.householdshopper.viewmodel.AddMemberViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun AddUserItem(user: User, viewModel: AddMemberViewModel) {

    var inviteStatus by remember {
        mutableStateOf(InviteStatus.NOT_INVITED)
    }
    val context = LocalContext.current

    LaunchedEffect(key1 = user) {
        inviteStatus = viewModel.getInviteStatus(user)
        println(inviteStatus)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            if (inviteStatus == InviteStatus.NOT_INVITED){
                IconButton(onClick = {
                    viewModel.inviteUser(context = context, invitee = user)
                    inviteStatus = InviteStatus.INVITED
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Invite"
                    )
                }
            }
            else{
                Text(
                    text = "The user has already been invited",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}