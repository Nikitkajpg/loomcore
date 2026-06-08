package com.njpg.loomcore.viewmodel

import androidx.lifecycle.ViewModel
import com.njpg.loomcore.data.JsonRepository
import com.njpg.loomcore.data.Paths
import com.njpg.loomcore.model.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class OrdersViewModel : ViewModel() {

    private val repo = JsonRepository(
        file = Paths.ordersFile, serializer = Order.serializer(), getId = { it.id })

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders = _orders.asStateFlow()

    init {
        _orders.value = repo.loadAll()
    }

    fun add(order: Order) {
        _orders.update { it + order }
        repo.saveAll(_orders.value)
    }

    fun update(order: Order) {
        _orders.update { list -> list.map { if (it.id == order.id) order else it } }
        repo.saveAll(_orders.value)
    }

    fun delete(id: Int) {
        _orders.update { list -> list.filter { it.id != id } }
        repo.saveAll(_orders.value)
    }

    val nextId: Int get() = repo.nextId(_orders.value)
}