package com.njpg.loomcore.ui.screen.tabs.orders

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.njpg.loomcore.model.Order
import com.njpg.loomcore.model.OrderStatus
import com.njpg.loomcore.ui.library.ConfirmDeleteDialog
import com.njpg.loomcore.ui.screen.tabs.TabScaffold
import com.njpg.loomcore.viewmodel.*

val statusColor: Map<OrderStatus, Color> = mapOf(
    OrderStatus.IN_PROGRESS to Color(0xFF1976D2),
    OrderStatus.DONE to Color(0xFF388E3C),
    OrderStatus.CANCELLED to Color(0xFF757575)
)

@Composable
fun OrdersTab(
    vm: OrdersViewModel,
    clientsVm: ClientsViewModel,
    materialsVm: MaterialsViewModel,
    productsVm: ProductsViewModel,
    profileVm: ProfileViewModel,
) {
    val orders by vm.orders.collectAsState()
    val clients by clientsVm.clients.collectAsState()
    val materials by materialsVm.materials.collectAsState()
    val products by productsVm.products.collectAsState()
    val profile by profileVm.profile.collectAsState()
    val priceListVm: PriceListViewModel = viewModel { PriceListViewModel() }
    val priceGroups by priceListVm.groups.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf<Order?>(null) }
    var itemToDelete by remember { mutableStateOf<Order?>(null) }

    fun openDialog(target: Order?) {
        editTarget = target; showDialog = true
    }

    fun closeDialog() {
        editTarget = null; showDialog = false
    }

    if (showDialog) {
        OrderDialog(
            initial = editTarget,
            nextId = vm.nextId,
            allClients = clients,
            allMaterials = materials,
            allProducts = products,
            allPriceGroups = priceGroups,
            profile = profile,
            onConfirm = { order ->
                if (editTarget == null) vm.add(order) else vm.update(order)
                closeDialog()
            },
            onDismiss = ::closeDialog
        )
    }

    itemToDelete?.let { order ->
        ConfirmDeleteDialog(
            itemName = orderTitle(order, products),
            onConfirm = { vm.delete(order.id); itemToDelete = null },
            onDismiss = { itemToDelete = null })
    }

    TabScaffold(
        isEmpty = orders.isEmpty(),
        emptyText = "Нет заказов. Нажмите + чтобы добавить.",
        onAdd = { openDialog(null) }) {
        items(orders, key = { it.id }) { order ->
            OrderCard(
                order = order,
                clients = clients,
                materials = materials,
                products = products,
                currency = profile.defaultCurrency,
                onEdit = { openDialog(order) },
                onDelete = { itemToDelete = order })
        }
    }
}

fun orderTitle(order: Order, products: List<com.njpg.loomcore.model.Product>): String = when (order.type) {
    com.njpg.loomcore.model.OrderType.PRODUCT -> products.find { it.id == order.productId }?.name
        ?: "Заказ #${order.id}"

    com.njpg.loomcore.model.OrderType.REPAIR -> order.repairOperations.firstOrNull()?.name?.ifBlank { null }
        ?: "Ремонт #${order.id}"
}