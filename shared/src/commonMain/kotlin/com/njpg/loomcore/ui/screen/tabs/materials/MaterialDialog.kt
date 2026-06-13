package com.njpg.loomcore.ui.screen.tabs.materials

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.njpg.loomcore.model.Material
import com.njpg.loomcore.model.Supplier

/**
 * Диалог создания и редактирования Материала.
 *
 * При создании [initial] = null, при редактировании — содержит
 * текущие данные материала. Поле [nextId] используется только при создании.
 *
 * @param title      Заголовок диалога.
 * @param initial    Текущие данные для режима редактирования, null для создания.
 * @param nextId     Идентификатор для нового материала (от [com.njpg.loomcore.viewmodel.MaterialsViewModel.nextId]).
 * @param onConfirm  Вызывается при нажатии "Сохранить" с готовым объектом [Material].
 * @param onDismiss  Вызывается при отмене или закрытии диалога.
 */
@Composable
fun MaterialDialog(
    title: String,
    initial: Material?,
    nextId: Int,
    suppliers: List<Supplier>,
    onConfirm: (Material) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(initial?.name ?: "") }
    var costPerUnit by remember { mutableStateOf(initial?.costPerUnit?.toString() ?: "") }
    var unitName by remember { mutableStateOf(initial?.unitName ?: "шт.") }
    var notes by remember { mutableStateOf(initial?.notes ?: "") }
    var selectedSupplierId by remember { mutableStateOf(initial?.supplierId) }
    var supplierDropdownExpanded by remember { mutableStateOf(false) }

    val selectedSupplierName = suppliers.find { it.id == selectedSupplierId }?.name ?: "Не выбран"

    AlertDialog(onDismissRequest = onDismiss, title = { Text(title) }, text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Название") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = costPerUnit,
                    onValueChange = { costPerUnit = it },
                    label = { Text("Цена за ед.") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = unitName,
                    onValueChange = { unitName = it },
                    label = { Text("Единица") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            if (suppliers.isNotEmpty()) {
                Box {
                    OutlinedTextField(
                        value = selectedSupplierName,
                        onValueChange = {},
                        label = { Text("Поставщик") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { supplierDropdownExpanded = true }) {
                                Icon(
                                    androidx.compose.material.icons.Icons.Default.ArrowDropDown,
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = supplierDropdownExpanded, onDismissRequest = { supplierDropdownExpanded = false }) {
                        DropdownMenuItem(text = { Text("Не выбран") }, onClick = {
                            selectedSupplierId = null
                            supplierDropdownExpanded = false
                        })
                        suppliers.forEach { supplier ->
                            DropdownMenuItem(text = { Text(supplier.name) }, onClick = {
                                selectedSupplierId = supplier.id
                                supplierDropdownExpanded = false
                            })
                        }
                    }
                }
            }
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Заметки") },
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }, confirmButton = {
        TextButton(onClick = {
            onConfirm(
                Material(
                    id = initial?.id ?: nextId,
                    name = name.trim(),
                    costPerUnit = costPerUnit.toDoubleOrNull() ?: 0.0,
                    unitName = unitName.trim().ifBlank { "шт." },
                    supplierId = selectedSupplierId,
                    notes = notes.trim()
                )
            )
        }) { Text("Сохранить") }
    }, dismissButton = {
        TextButton(onClick = onDismiss) { Text("Отмена") }
    })
}