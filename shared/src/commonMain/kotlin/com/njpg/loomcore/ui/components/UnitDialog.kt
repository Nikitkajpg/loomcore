package com.njpg.loomcore.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.njpg.loomcore.model.ProductUnit

@Composable
fun UnitDialog(
    initial: ProductUnit?, nextId: Int, onConfirm: (ProductUnit) -> kotlin.Unit, onDismiss: () -> kotlin.Unit
) {
    var number by remember { mutableStateOf(initial?.number ?: "") }
    var name by remember { mutableStateOf(initial?.name ?: "") }
    var cost by remember { mutableStateOf(initial?.cost?.toString() ?: "") }
    var price by remember { mutableStateOf(initial?.price?.toString() ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Новое изделие" else "Редактировать изделие") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = number,
                    onValueChange = { number = it },
                    label = { Text("Номер") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Изделие") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = cost,
                    onValueChange = { cost = it },
                    label = { Text("Затраты") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Цена") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val productUnit = ProductUnit(
                    id = initial?.id ?: nextId,
                    number = number.trim(),
                    name = name.trim(),
                    cost = cost.toDoubleOrNull() ?: 0.0,
                    price = price.toDoubleOrNull() ?: 0.0,
                    photoPath = initial?.photoPath
                )
                onConfirm(productUnit)
            }) { Text("Сохранить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        })
}