package com.householdshopper.view.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.householdshopper.model.User
import com.householdshopper.viewmodel.HouseholdViewModel

@Composable
fun UserItem(
    user: User,
    viewModel: HouseholdViewModel
) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 4.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.username.first().uppercaseChar().toString(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (viewModel.isOwner(user.documentId)) {
                    Text(
                        text = "Owner",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (!viewModel.isOwner(user.documentId)) {
                IconButton(
                    onClick = {
                        viewModel.removeMemberFromHousehold(userID = user.documentId, context = context)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove member",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}