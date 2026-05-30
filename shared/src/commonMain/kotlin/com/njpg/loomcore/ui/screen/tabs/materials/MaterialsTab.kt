package com.njpg.loomcore.ui.screen.tabs.materials

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import com.njpg.loomcore.model.Material
import com.njpg.loomcore.ui.library.ConfirmDeleteDialog
import com.njpg.loomcore.ui.screen.tabs.TabScaffold
import com.njpg.loomcore.viewmodel.MaterialsViewModel
import com.njpg.loomcore.viewmodel.ProductsViewModel
import com.njpg.loomcore.viewmodel.SuppliersViewModel

@Composable
fun MaterialsTab(vm: MaterialsViewModel, suppliersVm: SuppliersViewModel, productsVm: ProductsViewModel) {
    val materials by vm.materials.collectAsState()
    val suppliers by suppliersVm.suppliers.collectAsState()
    val products by productsVm.products.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf<Material?>(null) }
    var itemToDelete by remember { mutableStateOf<Material?>(null) }

    fun openDialog(target: Material?) {
        editTarget = target
        showDialog = true
    }

    fun closeDialog() {
        editTarget = null
        showDialog = false
    }

    if (showDialog) {
        MaterialDialog(
            title = if (editTarget == null) "Новый материал" else "Редактировать материал",
            initial = editTarget,
            nextId = vm.nextId(),
            suppliers = suppliers,
            onConfirm = { material ->
                if (editTarget == null) vm.add(material) else vm.update(material)
                closeDialog()
            },
            onDismiss = { closeDialog() })
    }

    itemToDelete?.let { material ->
        val linkedProducts = products.filter { p -> p.materialsUsed.any { it.materialId == material.id } }
        val warning = if (linkedProducts.isNotEmpty()) {
            "Этот материал используется в изделиях:\n" + linkedProducts.joinToString(", ") { it.name }
        } else null

        ConfirmDeleteDialog(itemName = material.name, warningText = warning, onConfirm = {
            vm.delete(material.id)
            itemToDelete = null
        }, onDismiss = { itemToDelete = null })
    }

    TabScaffold(
        isEmpty = materials.isEmpty(),
        emptyText = "Нет материалов. Нажмите + чтобы добавить.",
        onAdd = { openDialog(null) }) {
        items(materials, key = { it.id }) { material ->
            MaterialCard(
                material = material,
                supplier = suppliers.find { it.id == material.supplierId },
                onEdit = { openDialog(material) },
                onDelete = { itemToDelete = material })
        }
    }
}