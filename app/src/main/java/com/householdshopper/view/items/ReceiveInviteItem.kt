package com.householdshopper.view.items

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.householdshopper.model.Invite
import com.householdshopper.ui.theme.green
import com.householdshopper.ui.theme.red
import com.householdshopper.viewmodel.InvitesViewModel

@Composable
fun ReceiveInviteItem(invite: Invite, viewModel: InvitesViewModel){
    val name by remember {
        mutableStateOf(viewModel.getUser(invite.from).username)
    }
    val context = LocalContext.current
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 4.dp,
        shadowElevation = 8.dp
    ){
        Column (
            verticalArrangement = Arrangement.SpaceBetween
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(16.dp)
            ){
                Text(
                    text = "$name invites you to join his/her household",
                    fontSize = 12.sp
                )
            }
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ){
                Button(onClick = { viewModel.acceptRequest(invite = invite, context = context) }) {
                    Text(text = "Accept", color = green)
                }
                Button(onClick = { viewModel.declineRequest(invite = invite, context = context) }) {
                    Text(text = "Decline", color = red)
                }
            }
        }
    }
}