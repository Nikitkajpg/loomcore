package com.njpg.loomcore.ui.screen.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.njpg.loomcore.ui.components.UnitDialog
import com.njpg.loomcore.viewmodel.MainViewModel

@Composable
fun UnitsTab(vm: MainViewModel) {
    val units by vm.units.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf<com.njpg.loomcore.model.ProductUnit?>(null) }

    if (showDialog) {
        UnitDialog(initial = editTarget, nextId = vm.nextUnitId(), onConfirm = { unit ->
            if (editTarget == null) vm.addUnit(unit) else vm.updateUnit(unit)
            showDialog = false
            editTarget = null
        }, onDismiss = { showDialog = false; editTarget = null })
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { editTarget = null; showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить изделие")
            }
        }) { padding ->
        if (units.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Нет изделий. Нажмите + чтобы добавить.", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(units, key = { it.id }) { unit ->
                    UnitCard(
                        productUnit = unit,
                        onEdit = { editTarget = it; showDialog = true },
                        onDelete = { vm.deleteUnit(it.id) },
                        onPickPhoto = { picked ->
                            vm.importUnitPhoto(unit.id, picked.toPath())
                        })
                }
            }
        }
    }
}

