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
import com.njpg.loomcore.model.Client
import com.njpg.loomcore.ui.components.ContactDialog
import com.njpg.loomcore.ui.components.ContactDraft
import com.njpg.loomcore.viewmodel.ClientsViewModel

@Composable
fun ClientsTab(vm: ClientsViewModel) {
    val clients by vm.clients.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf<Client?>(null) }

    if (showDialog) {
        ContactDialog(
            title = if (editTarget == null) "Новый клиент" else "Редактировать клиента",
            initial = editTarget?.let { ContactDraft(it.id, it.name, it.contact, it.notes) },
            nextId = vm.nextId(),
            onConfirm = { d ->
                val c = Client(d.id, d.name, d.contact, d.notes)
                if (editTarget == null) vm.add(c) else vm.update(c)
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
        if (clients.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Нет клиентов.", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(clients, key = { it.id }) { c ->
                    ContactCard(
                        name = c.name,
                        contact = c.contact,
                        notes = c.notes,
                        onEdit = { editTarget = c; showDialog = true },
                        onDelete = { vm.delete(c.id) })
                }
            }
        }
    }
}