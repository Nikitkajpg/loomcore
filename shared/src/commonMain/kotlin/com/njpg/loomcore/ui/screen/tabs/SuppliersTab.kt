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
import com.njpg.loomcore.model.Supplier
import com.njpg.loomcore.ui.components.ContactDialog
import com.njpg.loomcore.ui.components.ContactDraft
import com.njpg.loomcore.viewmodel.MainViewModel

@Composable
fun SuppliersTab(vm: MainViewModel) {
    val suppliers by vm.suppliers.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf<Supplier?>(null) }

    if (showDialog) {
        ContactDialog(
            title = if (editTarget == null) "Новый поставщик" else "Редактировать поставщика",
            initial = editTarget?.let { ContactDraft(it.id, it.name, it.contact, it.notes) },
            nextId = vm.nextSupplierId(),
            onConfirm = { d ->
                val s = Supplier(d.id, d.name, d.contact, d.notes)
                if (editTarget == null) vm.addSupplier(s) else vm.updateSupplier(s)
                showDialog = false; editTarget = null
            },
            onDismiss = { showDialog = false; editTarget = null })
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { editTarget = null; showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        }) { padding ->
        if (suppliers.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Нет поставщиков.", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(suppliers, key = { it.id }) { s ->
                    ContactCard(
                        name = s.name,
                        contact = s.contact,
                        notes = s.notes,
                        onEdit = { editTarget = s; showDialog = true },
                        onDelete = { vm.deleteSupplier(s.id) })
                }
            }
        }
    }
}