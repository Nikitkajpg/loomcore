package com.njpg.loomcore.ui.screen.tabs.materials

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import com.njpg.loomcore.model.Material
import com.njpg.loomcore.ui.screen.tabs.TabScaffold
import com.njpg.loomcore.viewmodel.MaterialsViewModel
import com.njpg.loomcore.viewmodel.SuppliersViewModel

@Composable
fun MaterialsTab(vm: MaterialsViewModel, suppliersVm: SuppliersViewModel) {
    val materials by vm.materials.collectAsState()
    val suppliers by suppliersVm.suppliers.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf<Material?>(null) }

    fun openDialog(target: Material?) {
        editTarget = target; showDialog = true
    }

    fun closeDialog() {
        editTarget = null; showDialog = false
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

    TabScaffold(
        isEmpty = materials.isEmpty(),
        emptyText = "Нет материалов. Нажмите + чтобы добавить.",
        onAdd = { openDialog(null) }) {
        items(materials, key = { it.id }) { material ->
            MaterialCard(
                material = material,
                supplier = suppliers.find { it.id == material.supplierId },
                onEdit = { openDialog(material) },
                onDelete = { vm.delete(material.id) })
        }
    }
}