package com.njpg.loomcore.ui.screen.tabs.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.njpg.loomcore.model.PriceRow

@Composable
fun ServiceRow(
    row: PriceRow, onUpdate: (PriceRow) -> Unit, onDelete: () -> Unit
) {
    var service by remember(row.id) { mutableStateOf(row.service) }
    var price by remember(row.id) { mutableStateOf(row.price) }

    LaunchedEffect(service, price) {
        val updated = row.copy(service = service, price = price)
        if (updated != row) onUpdate(updated)
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        InlineCell(value = service, onValueChange = { service = it }, modifier = Modifier.weight(1f))
        InlineCell(value = price, onValueChange = { price = it }, modifier = Modifier.width(84.dp))
        IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
            Icon(
                Icons.Default.Delete,
                null,
                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}