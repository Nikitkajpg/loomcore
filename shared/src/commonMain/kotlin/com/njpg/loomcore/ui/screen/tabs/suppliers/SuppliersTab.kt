package com.njpg.loomcore.ui.screen.tabs.suppliers

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import com.njpg.loomcore.model.Supplier
import com.njpg.loomcore.ui.screen.tabs.TabScaffold
import com.njpg.loomcore.viewmodel.SuppliersViewModel

@Composable
fun SuppliersTab(vm: SuppliersViewModel) {
    val suppliers by vm.suppliers.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf<Supplier?>(null) }

    fun openDialog(target: Supplier?) {
        editTarget = target
        showDialog = true
    }

    fun closeDialog() {
        editTarget = null
        showDialog = false
    }

    if (showDialog) {
        SupplierDialog(
            title = if (editTarget == null) "Новый поставщик" else "Редактировать поставщика",
            initial = editTarget,
            nextId = vm.nextId(),
            onConfirm = { supplier ->
                if (editTarget == null) vm.add(supplier) else vm.update(supplier)
                closeDialog()
            },
            onDismiss = { closeDialog() })
    }

    TabScaffold(
        isEmpty = suppliers.isEmpty(),
        emptyText = "Нет поставщиков. Нажмите + чтобы добавить.",
        onAdd = { openDialog(null) }) {
        items(suppliers, key = { it.id }) { supplier ->
            SupplierCard(supplier = supplier, onEdit = { openDialog(supplier) }, onDelete = { vm.delete(supplier.id) })
        }
    }
}