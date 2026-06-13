package com.njpg.loomcore.viewmodel

import androidx.lifecycle.ViewModel
import com.njpg.loomcore.data.JsonRepository
import com.njpg.loomcore.data.Paths
import com.njpg.loomcore.model.Supplier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel для раздела "Поставщики".
 */
class SuppliersViewModel : ViewModel() {

    private val repo = JsonRepository(
        file = Paths.suppliersFile, serializer = Supplier.serializer(), getId = { it.id })

    private val _suppliers = MutableStateFlow<List<Supplier>>(emptyList())

    /** Реактивный список поставщиков. */
    val suppliers = _suppliers.asStateFlow()

    init {
        _suppliers.value = repo.loadAll()
    }

    /** Добавляет нового поставщика в конец списка и сохраняет. */
    fun add(supplier: Supplier) {
        _suppliers.update { it + supplier }
        repo.saveAll(_suppliers.value)
    }

    /** Обновляет существующего поставщика (ищет по [Supplier.id]) и сохраняет. */
    fun update(supplier: Supplier) {
        _suppliers.update { list -> list.map { if (it.id == supplier.id) supplier else it } }
        repo.saveAll(_suppliers.value)
    }

    /** Удаляет поставщика по [id] и сохраняет. */
    fun delete(id: Int) {
        _suppliers.update { list -> list.filter { it.id != id } }
        repo.saveAll(_suppliers.value)
    }

    /** Возвращает следующий свободный идентификатор. */
    fun nextId(): Int = repo.nextId(_suppliers.value)
}