package com.njpg.loomcore.ui.screen.tabs.materials

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.njpg.loomcore.model.Material
import com.njpg.loomcore.model.Supplier

/**
 * Карточка материала в списке раздела "Материалы".
 *
 * Отображает название, цену за единицу, поставщика (если есть) и заметки.
 *
 * @param material   Отображаемый материал.
 * @param supplier   Поставщик этого материала (из [Material.supplierId]), или null.
 * @param onEdit     Открывает [MaterialDialog] для редактирования.
 * @param onDelete   Открывает [ConfirmDeleteDialog] для удаления.
 */
@Composable
fun MaterialCard(
    material: Material, supplier: Supplier?, onEdit: () -> Unit, onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(material.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${material.costPerUnit} Br / ${material.unitName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                if (supplier != null) {
                    Text(
                        text = "Поставщик: ${supplier.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (material.notes.isNotBlank()) {
                    Text(
                        text = material.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Редактировать")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}