package com.njpg.loomcore.viewmodel

import com.njpg.loomcore.data.Paths
import com.njpg.loomcore.model.Order

/**
 * ViewModel для раздела "Заказы".
 */
class OrdersViewModel : BaseViewModel<Order>(
    file = Paths.ordersFile, serializer = Order.serializer()
) {
    val orders = items
}