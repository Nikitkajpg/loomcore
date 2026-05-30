package com.njpg.loomcore.ui.screen.tabs.clients

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import com.njpg.loomcore.model.Client
import com.njpg.loomcore.ui.screen.tabs.TabScaffold
import com.njpg.loomcore.viewmodel.ClientsViewModel

@Composable
fun ClientsTab(vm: ClientsViewModel) {
    val clients by vm.clients.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf<Client?>(null) }

    fun openDialog(target: Client?) {
        editTarget = target
        showDialog = true
    }

    fun closeDialog() {
        editTarget = null
        showDialog = false
    }

    if (showDialog) {
        ClientDialog(
            title = if (editTarget == null) "Новый клиент" else "Редактировать клиента",
            initial = editTarget,
            nextId = vm.nextId(),
            onConfirm = { client ->
                if (editTarget == null) vm.add(client) else vm.update(client)
                closeDialog()
            },
            onDismiss = { closeDialog() })
    }

    TabScaffold(
        isEmpty = clients.isEmpty(), emptyText = "Нет клиентов. Нажмите + чтобы добавить.", onAdd = { openDialog(null) }) {
        items(clients, key = { it.id }) { client ->
            ClientCard(client = client, onEdit = { openDialog(client) }, onDelete = { vm.delete(client.id) })
        }
    }
}