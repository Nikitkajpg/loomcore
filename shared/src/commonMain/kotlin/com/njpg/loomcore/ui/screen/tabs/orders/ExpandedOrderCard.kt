package com.njpg.loomcore.ui.screen.tabs.orders

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.njpg.loomcore.model.Material
import com.njpg.loomcore.model.Order
import com.njpg.loomcore.model.OrderType

/**
 * Развёрнутое содержимое карточки заказа.
 *
 * ## Содержимое по типу заказа
 *
 * ### [OrderType.PRODUCT] (Пошив)
 * - Список материалов: `название × количество единица` (поиск по [Material.id]).
 * - Время выполнения в часах.
 *
 * ### [OrderType.REPAIR] (Ремонт)
 * - Список операций с названием, количеством и суммой по строке.
 * - Итоговая сумма — берётся из кэшированного [Order.repairTotalPrice].
 *
 * В конце обоих типов отображаются заметки, если они не пустые.
 *
 * @param order     Заказ с данными для отображения.
 * @param materials Список материалов — для поиска имён по id.
 * @param currency  Символ валюты для форматирования цен.
 */
@Composable
fun ExpandedOrderCard(
    order: Order, materials: List<Material>, currency: String
) {
    Column(modifier = Modifier.padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        HorizontalDivider(modifier = Modifier.padding(bottom = 4.dp))
        when (order.type) {
            OrderType.PRODUCT -> {
                if (order.materialsUsed.isNotEmpty()) {
                    Text(
                        "Материалы:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    order.materialsUsed.forEach { usage ->
                        val mat = materials.find { it.id == usage.materialId }
                        Text(
                            "  ${mat?.name ?: "?"} × ${usage.amount} ${mat?.unitName ?: ""}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                if (order.workTimeHours > 0) {
                    Text("Время: ${order.workTimeHours} ч.", style = MaterialTheme.typography.bodySmall)
                }
            }

            OrderType.REPAIR -> {
                if (order.repairOperations.isNotEmpty()) {
                    Text(
                        "Операции:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    order.repairOperations.forEach { op ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "${op.name} × ${op.quantity}",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                "%.2f $currency".format(op.totalPrice),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Итого:", style = MaterialTheme.typography.labelSmall)
                        Text(
                            "%.2f $currency".format(order.repairTotalPrice),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
        if (order.notes.isNotBlank()) {
            Text(
                order.notes,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}