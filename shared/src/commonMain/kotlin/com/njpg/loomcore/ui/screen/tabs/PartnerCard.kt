package com.njpg.loomcore.ui.screen.tabs

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

/**
 * Универсальная карточка контрагента (клиента или поставщика).
 *
 * Отображает имя жирным заголовком, а под ним — произвольные строки
 * (телефон, ссылка, заметки). Пустые строки автоматически пропускаются.
 *
 * Повторно используется в [ClientCard] и [SupplierCard],
 * чтобы не дублировать разметку.
 *
 * @param name      Главное название (клиент или поставщик).
 * @param lines     Список дополнительных строк для отображения.
 * @param onEdit    Вызывается при нажатии кнопки редактирования.
 * @param onDelete  Вызывается при нажатии кнопки удаления.
 */
@Composable
fun PartnerCard(
    name: String, lines: List<String>, onEdit: () -> Unit, onDelete: () -> Unit
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
            Column(
                modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(name, style = MaterialTheme.typography.titleMedium)
                lines.forEach { line ->
                    if (line.isNotBlank()) {
                        Text(
                            text = line,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Редактировать")
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete, contentDescription = "Удалить", tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}