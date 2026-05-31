package com.njpg.loomcore.ui.screen.tabs.suppliers

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import com.njpg.loomcore.model.Supplier
import com.njpg.loomcore.ui.library.ConfirmDeleteDialog
import com.njpg.loomcore.ui.screen.tabs.TabScaffold
import com.njpg.loomcore.viewmodel.MaterialsViewModel
import com.njpg.loomcore.viewmodel.SuppliersViewModel

@Composable
fun SuppliersTab(vm: SuppliersViewModel, materialsVm: MaterialsViewModel) {
    val suppliers by vm.suppliers.collectAsState()
    val materials by materialsVm.materials.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf<Supplier?>(null) }
    var itemToDelete by remember { mutableStateOf<Supplier?>(null) }

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

    itemToDelete?.let { supplier ->
        val linkedMaterials = materials.filter { m -> m.supplierId == supplier.id }
        val warning = if (linkedMaterials.isNotEmpty()) {
            "Этот поставщик привязан к материалам:\n" + linkedMaterials.joinToString(", ") { it.name }
        } else null

        ConfirmDeleteDialog(itemName = supplier.name, warningText = warning, onConfirm = {
            vm.delete(supplier.id)
            itemToDelete = null
        }, onDismiss = { itemToDelete = null })
    }

    TabScaffold(
        isEmpty = suppliers.isEmpty(),
        emptyText = "Нет поставщиков. Нажмите + чтобы добавить.",
        onAdd = { openDialog(null) }) {
        items(suppliers, key = { it.id }) { supplier ->
            SupplierCard(supplier = supplier, onEdit = { openDialog(supplier) }, onDelete = { itemToDelete = supplier })
        }
    }
}