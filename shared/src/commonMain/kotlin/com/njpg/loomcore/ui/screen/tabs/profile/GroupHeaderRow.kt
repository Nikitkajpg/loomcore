package com.njpg.loomcore.ui.screen.tabs.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.njpg.loomcore.model.PriceGroup

@Composable
fun GroupHeaderRow(
    group: PriceGroup, onNameChange: (String) -> Unit, onDelete: () -> Unit
) {
    var name by remember(group.id) { mutableStateOf(group.name) }

    Row(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f))
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BasicTextField(
            value = name,
            onValueChange = { name = it },
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            ),
            singleLine = true,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier.weight(1f),
            decorationBox = { inner ->
                Box(Modifier.fillMaxWidth()) { inner() }
            })

        LaunchedEffect(name) {
            if (name != group.name) onNameChange(name)
        }

        IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
            Icon(
                Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp)
            )
        }
    }
}