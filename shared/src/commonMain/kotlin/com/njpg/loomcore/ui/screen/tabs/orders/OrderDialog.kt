package com.njpg.loomcore.ui.screen.tabs.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.njpg.loomcore.core.DateVisualTransformation
import com.njpg.loomcore.core.formatAsDate
import com.njpg.loomcore.model.*
import com.njpg.loomcore.ui.screen.tabs.products.MaterialUsageRow

private fun String.toDoubleLocale() = replace(',', '.').toDoubleOrNull()

@Composable
fun OrderDialog(
    initial: Order?,
    nextId: Int,
    allClients: List<Client>,
    allMaterials: List<Material>,
    allProducts: List<Product>,
    allPriceGroups: List<PriceGroup>,
    profile: Profile,
    onConfirm: (Order) -> Unit,
    onDismiss: () -> Unit,
) {
    var type by remember { mutableStateOf(initial?.type ?: OrderType.PRODUCT) }
    var clientId by remember { mutableStateOf(initial?.clientId) }
    var status by remember { mutableStateOf(initial?.status ?: OrderStatus.IN_PROGRESS) }
    var startDate by remember { mutableStateOf(initial?.startDate?.filter { it.isDigit() } ?: "") }
    var endDate by remember { mutableStateOf(initial?.endDate?.filter { it.isDigit() } ?: "") }
    var notes by remember { mutableStateOf(initial?.notes ?: "") }
    var productId by remember { mutableStateOf(initial?.productId) }
    var workTimeHours by remember { mutableStateOf(initial?.workTimeHours?.toString() ?: "") }
    val usageRows = remember {
        mutableStateListOf(
            *(initial?.materialsUsed?.map { it.materialId to it.amount.toString() }?.toTypedArray() ?: emptyArray())
        )
    }

    data class RepairRow(
        val priceRowId: Int?,
        val name: String,
        val quantity: String,
        val unitPrice: String,
    )

    val repairRows = remember {
        mutableStateListOf(
            *(initial?.repairOperations?.map { op ->
                RepairRow(
                    priceRowId = op.priceRowId,
                    name = op.name,
                    quantity = op.quantity.toString(),
                    unitPrice = op.unitPrice.toString()
                )
            }?.toTypedArray() ?: emptyArray())
        )
    }

    var clientDropdown by remember { mutableStateOf(false) }
    var productDropdown by remember { mutableStateOf(false) }
    var statusDropdown by remember { mutableStateOf(false) }

    val repairDropdownOpen = remember { mutableStateListOf<Boolean>() }
    while (repairDropdownOpen.size < repairRows.size) repairDropdownOpen.add(false)
    while (repairDropdownOpen.size > repairRows.size) repairDropdownOpen.removeLastOrNull()

    val selectedClientName = allClients.find { it.id == clientId }?.name ?: "Не выбран"
    val selectedProductName = allProducts.find { it.id == productId }?.name ?: "Не выбрано"

    val previewCost by remember(usageRows.toList(), workTimeHours, type) {
        derivedStateOf {
            if (type != OrderType.PRODUCT) return@derivedStateOf 0.0
            val matCost = usageRows.sumOf { (matId, amtStr) ->
                (allMaterials.find { it.id == matId }?.costPerUnit ?: 0.0) * (amtStr.toDoubleLocale() ?: 0.0)
            }
            matCost + (workTimeHours.toDoubleLocale() ?: 0.0) * profile.hourlyRate
        }
    }
    val previewPrice = previewCost * (1.0 + profile.markupPercent / 100.0)

    val repairTotal by remember(repairRows.toList(), type) {
        derivedStateOf {
            if (type != OrderType.REPAIR) return@derivedStateOf 0.0
            repairRows.sumOf { row ->
                (row.unitPrice.toDoubleLocale() ?: 0.0) * (row.quantity.toDoubleLocale() ?: 1.0)
            }
        }
    }

    val startDateValid = startDate.length == 8

    fun addRepairRow(priceRowId: Int?, name: String, unitPrice: Double) {
        repairRows.add(
            RepairRow(
                priceRowId = priceRowId, name = name, quantity = "1", unitPrice = unitPrice.toString()
            )
        )
        repairDropdownOpen.add(false)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Новый заказ" else "Редактировать заказ") },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().heightIn(max = 580.dp)
            ) {
                item {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OrderType.entries.forEach { t ->
                            FilterChip(
                                selected = type == t,
                                onClick = { type = t },
                                label = { Text(t.label) },
                                modifier = Modifier.weight(1f).padding(end = if (t == OrderType.PRODUCT) 4.dp else 0.dp)
                            )
                        }
                    }
                }

                item {
                    Box {
                        OutlinedTextField(
                            value = selectedClientName,
                            onValueChange = {},
                            label = { Text("Клиент") },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { clientDropdown = true }) {
                                    Icon(Icons.Default.ArrowDropDown, null)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        DropdownMenu(expanded = clientDropdown, onDismissRequest = { clientDropdown = false }) {
                            DropdownMenuItem(
                                text = { Text("Не выбран") },
                                onClick = { clientId = null; clientDropdown = false })
                            allClients.forEach { c ->
                                DropdownMenuItem(
                                    text = { Text(c.name) },
                                    onClick = { clientId = c.id; clientDropdown = false })
                            }
                        }
                    }
                }

                item {
                    Box {
                        OutlinedTextField(
                            value = status.label,
                            onValueChange = {},
                            label = { Text("Статус") },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { statusDropdown = true }) {
                                    Icon(Icons.Default.ArrowDropDown, null)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        DropdownMenu(expanded = statusDropdown, onDismissRequest = { statusDropdown = false }) {
                            OrderStatus.entries.forEach { s ->
                                DropdownMenuItem(
                                    text = { Text(s.label) },
                                    onClick = { status = s; statusDropdown = false })
                            }
                        }
                    }
                }

                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = startDate,
                            onValueChange = { v ->
                                val d = v.filter { it.isDigit() }; if (d.length <= 8) startDate = d
                            },
                            label = { Text("Дата начала *") },
                            placeholder = { Text("ДД.ММ.ГГГГ") },
                            singleLine = true,
                            isError = startDate.isNotEmpty() && !startDateValid,
                            visualTransformation = DateVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = endDate,
                            onValueChange = { v -> val d = v.filter { it.isDigit() }; if (d.length <= 8) endDate = d },
                            label = { Text("Дата конца") },
                            placeholder = { Text("ДД.ММ.ГГГГ") },
                            singleLine = true,
                            visualTransformation = DateVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                if (type == OrderType.PRODUCT) {
                    if (allProducts.isNotEmpty()) {
                        item {
                            Box {
                                OutlinedTextField(
                                    value = selectedProductName,
                                    onValueChange = {},
                                    label = { Text("Изделие (шаблон)") },
                                    readOnly = true,
                                    trailingIcon = {
                                        IconButton(onClick = { productDropdown = true }) {
                                            Icon(Icons.Default.ArrowDropDown, null)
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                DropdownMenu(
                                    expanded = productDropdown, onDismissRequest = { productDropdown = false }) {
                                    DropdownMenuItem(
                                        text = { Text("Не выбрано") },
                                        onClick = { productId = null; productDropdown = false })
                                    allProducts.forEach { p ->
                                        DropdownMenuItem(text = { Text(p.name) }, onClick = {
                                            productId = p.id
                                            usageRows.clear()
                                            usageRows.addAll(p.materialsUsed.map { it.materialId to it.amount.toString() })
                                            workTimeHours = p.workTimeHours.toString()
                                            productDropdown = false
                                        })
                                    }
                                }
                            }
                        }
                    }

                    item {
                        HorizontalDivider()
                        Spacer(Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Себестоимость:", style = MaterialTheme.typography.bodySmall)
                            Text(
                                "%.2f ${profile.defaultCurrency}".format(previewCost),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Цена (+${profile.markupPercent}%):", style = MaterialTheme.typography.bodySmall)
                            Text(
                                "%.2f ${profile.defaultCurrency}".format(previewPrice),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                if (type == OrderType.REPAIR) {
                    item {
                        Text("Операции", style = MaterialTheme.typography.labelLarge)
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                "Операция",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                "Кол-во",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.width(72.dp)
                            )
                            Text(
                                "Цена/ед.",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.width(80.dp)
                            )
                            Spacer(Modifier.width(36.dp))
                        }
                    }

                    itemsIndexed(repairRows) { index, row ->
                        while (repairDropdownOpen.size <= index) repairDropdownOpen.add(false)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                OutlinedTextField(
                                    value = row.name.ifBlank { "Выбрать..." },
                                    onValueChange = {},
                                    readOnly = true,
                                    singleLine = true,
                                    trailingIcon = {
                                        IconButton(onClick = { repairDropdownOpen[index] = true }) {
                                            Icon(Icons.Default.ArrowDropDown, null)
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                DropdownMenu(
                                    expanded = repairDropdownOpen.getOrElse(index) { false },
                                    onDismissRequest = { repairDropdownOpen[index] = false }) {
                                    DropdownMenuItem(text = {
                                        Text(
                                            "Ввести вручную", color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }, onClick = {
                                        repairRows[index] = row.copy(priceRowId = null, name = "", unitPrice = "0")
                                        repairDropdownOpen[index] = false
                                    })
                                    HorizontalDivider()
                                    if (allPriceGroups.isEmpty()) {
                                        DropdownMenuItem(text = {
                                            Text(
                                                "Прейскурант пуст", color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }, onClick = {})
                                    } else {
                                        allPriceGroups.forEach { group ->
                                            DropdownMenuItem(text = {
                                                Text(
                                                    group.name,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }, enabled = false, onClick = {})
                                            group.rows.forEach { priceRow ->
                                                val unitPrice = priceRow.price.replace(',', '.').toDoubleOrNull() ?: 0.0
                                                DropdownMenuItem(text = {
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween
                                                    ) {
                                                        Text(priceRow.service, modifier = Modifier.weight(1f))
                                                        Text(
                                                            "${priceRow.price} ${profile.defaultCurrency}",
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )
                                                    }
                                                }, onClick = {
                                                    repairRows[index] = row.copy(
                                                        priceRowId = priceRow.id,
                                                        name = priceRow.service,
                                                        unitPrice = unitPrice.toString()
                                                    )
                                                    repairDropdownOpen[index] = false
                                                })
                                            }
                                        }
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = row.quantity,
                                onValueChange = { repairRows[index] = row.copy(quantity = it) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.width(72.dp)
                            )

                            OutlinedTextField(
                                value = row.unitPrice,
                                onValueChange = { repairRows[index] = row.copy(unitPrice = it, priceRowId = null) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.width(80.dp)
                            )

                            IconButton(onClick = {
                                repairRows.removeAt(index)
                                if (index < repairDropdownOpen.size) repairDropdownOpen.removeAt(index)
                            }) {
                                Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }

                    item {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = { addRepairRow(null, "", 0.0) }) {
                                Icon(Icons.Default.Add, null)
                                Spacer(Modifier.width(4.dp))
                                Text("Добавить операцию")
                            }
                        }
                    }

                    if (repairRows.isNotEmpty()) {
                        item {
                            HorizontalDivider()
                            Spacer(Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Итого:", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    "%.2f ${profile.defaultCurrency}".format(repairTotal),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Примечания") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val usages =
                        if (type == OrderType.PRODUCT) usageRows.filter { (_, amt) -> amt.toDoubleLocale() != null }
                            .map { (matId, amt) -> MaterialUsage(matId, amt.toDoubleLocale()!!) }
                        else emptyList()

                    val matCost = usages.sumOf { u ->
                        (allMaterials.find { it.id == u.materialId }?.costPerUnit ?: 0.0) * u.amount
                    }
                    val labor = (workTimeHours.toDoubleLocale() ?: 0.0) * profile.hourlyRate
                    val cost = matCost + labor
                    val price = cost * (1.0 + profile.markupPercent / 100.0)

                    val repairOps =
                        if (type == OrderType.REPAIR) repairRows.filter { it.name.isNotBlank() }.map { row ->
                            RepairOperation(
                                name = row.name,
                                quantity = row.quantity.toDoubleLocale() ?: 1.0,
                                priceRowId = row.priceRowId,
                                unitPrice = row.unitPrice.toDoubleLocale() ?: 0.0
                            )
                        }
                        else emptyList()

                    onConfirm(
                        Order(
                            id = initial?.id ?: nextId,
                            type = type,
                            clientId = clientId,
                            status = status,
                            startDate = formatAsDate(startDate),
                            endDate = formatAsDate(endDate).ifBlank { null },
                            productId = if (type == OrderType.PRODUCT) productId else null,
                            materialsUsed = usages,
                            workTimeHours = if (type == OrderType.PRODUCT) workTimeHours.toDoubleLocale()
                                ?: 0.0 else 0.0,
                            cachedCost = if (type == OrderType.PRODUCT) cost else 0.0,
                            finalPrice = if (type == OrderType.PRODUCT) price else 0.0,
                            repairOperations = repairOps,
                            repairTotalPrice = if (type == OrderType.REPAIR) repairTotal else 0.0,
                            notes = notes.trim()
                        )
                    )
                }, enabled = startDateValid
            ) { Text("Сохранить") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } })
}