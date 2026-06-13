package com.njpg.loomcore.viewmodel

import androidx.lifecycle.ViewModel
import com.njpg.loomcore.data.JsonRepository
import com.njpg.loomcore.data.Paths
import com.njpg.loomcore.model.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel для раздела "Заказы".
 *
 * Отличие от других ViewModel: [nextId] — свойство (val get()),
 * а не функция, так как вычисляется каждый раз при обращении и не
 * требует ручного вызова.
 */
class OrdersViewModel : ViewModel() {

    private val repo = JsonRepository(
        file = Paths.ordersFile, serializer = Order.serializer(), getId = { it.id })

    private val _orders = MutableStateFlow<List<Order>>(emptyList())

    /** Реактивный список заказов. */
    val orders = _orders.asStateFlow()

    init {
        _orders.value = repo.loadAll()
    }

    /** Добавляет новый заказ в конец списка и сохраняет. */
    fun add(order: Order) {
        _orders.update { it + order }
        repo.saveAll(_orders.value)
    }

    /** Обновляет существующий заказ (ищет по [Order.id]) и сохраняет. */
    fun update(order: Order) {
        _orders.update { list -> list.map { if (it.id == order.id) order else it } }
        repo.saveAll(_orders.value)
    }

    /** Удаляет заказ по [id] и сохраняет. */
    fun delete(id: Int) {
        _orders.update { list -> list.filter { it.id != id } }
        repo.saveAll(_orders.value)
    }

    /** Возвращает следующий свободный идентификатор. */
    fun nextId(): Int = repo.nextId(_orders.value)
}