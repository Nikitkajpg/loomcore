package com.njpg.loomcore.ui.screen.tabs.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.njpg.loomcore.viewmodel.PriceListViewModel

@Composable
fun PriceListTable(vm: PriceListViewModel, modifier: Modifier = Modifier) {
    val groups by vm.groups.collectAsState()

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Прейскурант", style = MaterialTheme.typography.titleMedium)
            FilledTonalButton(onClick = { vm.addGroup() }) {
                Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Добавить группу")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().background(
                MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
            ).padding(horizontal = 12.dp, vertical = 6.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Услуга",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
            Text(
                "Цена",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.width(84.dp)
            )
            Spacer(Modifier.width(36.dp))
        }

        if (groups.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp), contentAlignment = Alignment.Center
            ) {
                Text(
                    "Нет групп. Нажмите «Добавить группу».",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                groups.forEach { group ->
                    item(key = "group-${group.id}") {
                        GroupHeaderRow(
                            group = group,
                            onNameChange = { vm.updateGroupName(group.id, it) },
                            onDelete = { vm.deleteGroup(group.id) })
                    }

                    items(group.rows, key = { "row-${group.id}-${it.id}" }) { row ->
                        ServiceRow(
                            row = row,
                            onUpdate = { vm.updateRow(group.id, it) },
                            onDelete = { vm.deleteRow(group.id, row.id) })
                        HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    }

                    item(key = "add-row-${group.id}") {
                        TextButton(
                            onClick = { vm.addRow(group.id) }, modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Icon(Icons.Default.Add, null, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Добавить строку", style = MaterialTheme.typography.labelSmall)
                        }
                        HorizontalDivider(
                            thickness = 1.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }
    }
}