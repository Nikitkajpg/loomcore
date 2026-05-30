package com.njpg.loomcore.ui.screen.tabs.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.njpg.loomcore.model.Material

@Composable
fun MaterialUsageRow(
    materialId: Int,
    amount: String,
    allMaterials: List<Material>,
    onMaterialChange: (Int) -> Unit,
    onAmountChange: (String) -> Unit,
    onDelete: () -> Unit
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    val selectedMaterial = allMaterials.find { it.id == materialId }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            OutlinedTextField(
                value = selectedMaterial?.name ?: "",
                onValueChange = {},
                label = { Text("Материал") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { dropdownExpanded = true }) {
                        Icon(Icons.Default.ArrowDropDown, null)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            DropdownMenu(
                expanded = dropdownExpanded, onDismissRequest = { dropdownExpanded = false }) {
                allMaterials.forEach { mat ->
                    DropdownMenuItem(text = { Text("${mat.name} (${mat.unitName})") }, onClick = {
                        onMaterialChange(mat.id)
                        dropdownExpanded = false
                    })
                }
            }
        }
        OutlinedTextField(
            value = amount,
            onValueChange = onAmountChange,
            label = { Text("Кол-во") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.width(80.dp)
        )
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error)
        }
    }
}