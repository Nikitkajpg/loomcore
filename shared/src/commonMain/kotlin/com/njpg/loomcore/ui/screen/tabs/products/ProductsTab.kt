package com.njpg.loomcore.ui.screen.tabs.products

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import com.njpg.loomcore.model.Product
import com.njpg.loomcore.ui.library.ConfirmDeleteDialog
import com.njpg.loomcore.ui.screen.tabs.TabScaffold
import com.njpg.loomcore.ui.screen.tabs.products.card.ProductCard
import com.njpg.loomcore.viewmodel.MaterialsViewModel
import com.njpg.loomcore.viewmodel.ProductsViewModel

/**
 * Вкладка "Изделия" — каталог шаблонов изделий с CRUD-операциями.
 *
 * @param vm          ViewModel изделий.
 * @param materialsVm ViewModel материалов — для выбора в редакторе изделия.
 */
@Composable
fun ProductsTab(
    vm: ProductsViewModel, materialsVm: MaterialsViewModel
) {
    val products by vm.products.collectAsState()
    val materials by materialsVm.materials.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf<Product?>(null) }
    var itemToDelete by remember { mutableStateOf<Product?>(null) }

    fun openDialog(target: Product?) {
        editTarget = target; showDialog = true
    }

    fun closeDialog() {
        editTarget = null; showDialog = false
    }

    if (showDialog) {
        ProductDialog(initial = editTarget, nextId = vm.nextId(), allMaterials = materials, onConfirm = { product ->
            if (editTarget == null) vm.add(product) else vm.update(product)
            closeDialog()
        }, onDismiss = { closeDialog() })
    }

    itemToDelete?.let { product ->
        ConfirmDeleteDialog(itemName = product.name, onConfirm = {
            vm.delete(product.id)
            itemToDelete = null
        }, onDismiss = { itemToDelete = null })
    }

    TabScaffold(
        isEmpty = products.isEmpty(),
        emptyText = "Нет изделий. Нажмите + чтобы добавить.",
        onAdd = { openDialog(null) }) {
        items(products, key = { it.id }) { product ->
            ProductCard(
                product = product,
                allMaterials = materials,
                onEdit = { openDialog(product) },
                onDelete = { itemToDelete = product })
        }
    }
}
