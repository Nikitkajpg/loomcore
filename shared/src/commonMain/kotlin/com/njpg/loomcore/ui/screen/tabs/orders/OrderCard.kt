package com.njpg.loomcore.ui.screen.tabs.orders

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.njpg.loomcore.model.*

@Composable
fun OrderCard(
    order: Order,
    clients: List<Client>,
    materials: List<Material>,
    products: List<Product>,
    currency: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val client = clients.find { it.id == order.clientId }
    val statusCol = statusColor[order.status] ?: MaterialTheme.colorScheme.primary

    Card(
        modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.width(4.dp).height(48.dp).clip(RoundedCornerShape(2.dp)).background(
                        if (order.type == OrderType.REPAIR) MaterialTheme.colorScheme.tertiary
                        else MaterialTheme.colorScheme.primary
                    )
                )

                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (order.type == OrderType.REPAIR) MaterialTheme.colorScheme.tertiaryContainer
                            else MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                order.type.label,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(4.dp), color = statusCol.copy(alpha = 0.15f)
                        ) {
                            Text(
                                order.status.label,
                                style = MaterialTheme.typography.labelSmall,
                                color = statusCol,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(orderTitle(order, products), style = MaterialTheme.typography.titleMedium)
                    if (client != null) {
                        Text(
                            client.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        buildString {
                            append(order.startDate)
                            if (!order.endDate.isNullOrBlank()) append(" — ${order.endDate}")
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                when (order.type) {
                    OrderType.PRODUCT -> Column(horizontalAlignment = Alignment.End) {
                        Text("%.2f $currency".format(order.cachedCost), style = MaterialTheme.typography.bodySmall)
                        Text(
                            "%.2f $currency".format(order.finalPrice),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    OrderType.REPAIR -> if (order.repairTotalPrice > 0) {
                        Text(
                            "%.2f $currency".format(order.repairTotalPrice),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, null) }
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error
                        )
                    }
                    Icon(
                        if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
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
        }
    }
}