package com.njpg.loomcore.ui.screen.tabs.clients

import androidx.compose.runtime.Composable
import com.njpg.loomcore.model.Client
import com.njpg.loomcore.ui.screen.tabs.PartnerCard

@Composable
fun ClientCard(
    client: Client, onEdit: () -> Unit, onDelete: () -> Unit
) {
    PartnerCard(
        name = client.name,
        lines = listOf(client.phoneNumber, client.url, client.notes),
        onEdit = onEdit,
        onDelete = onDelete
    )
}