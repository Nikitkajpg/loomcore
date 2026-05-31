package com.njpg.loomcore.ui.screen.tabs.clients

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import com.njpg.loomcore.model.Client
import com.njpg.loomcore.ui.library.ConfirmDeleteDialog
import com.njpg.loomcore.ui.screen.tabs.TabScaffold
import com.njpg.loomcore.viewmodel.ClientsViewModel
import com.njpg.loomcore.viewmodel.ProductsViewModel

@Composable
fun ClientsTab(vm: ClientsViewModel, productsVm: ProductsViewModel) {
    val clients by vm.clients.collectAsState()
    val products by productsVm.products.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf<Client?>(null) }
    var itemToDelete by remember { mutableStateOf<Client?>(null) }

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
            title = if (editTarget == null) "Новый покупатель" else "Редактировать покупателя",
            initial = editTarget,
            nextId = vm.nextId(),
            onConfirm = { client ->
                if (editTarget == null) vm.add(client) else vm.update(client)
                closeDialog()
            },
            onDismiss = { closeDialog() })
    }

    itemToDelete?.let { client ->
        val linkedProducts = products.filter { p -> client.id in p.clientIds }
        val warning = if (linkedProducts.isNotEmpty()) {
            "Этот покупатель привязан к изделиям:\n" + linkedProducts.joinToString(", ") { it.name }
        } else null

        ConfirmDeleteDialog(itemName = client.name, warningText = warning, onConfirm = {
            vm.delete(client.id)
            itemToDelete = null
        }, onDismiss = { itemToDelete = null })
    }

    TabScaffold(
        isEmpty = clients.isEmpty(),
        emptyText = "Нет покупателей. Нажмите + чтобы добавить.",
        onAdd = { openDialog(null) }) {
        items(clients, key = { it.id }) { client ->
            ClientCard(client = client, onEdit = { openDialog(client) }, onDelete = { itemToDelete = client })
        }
    }
}