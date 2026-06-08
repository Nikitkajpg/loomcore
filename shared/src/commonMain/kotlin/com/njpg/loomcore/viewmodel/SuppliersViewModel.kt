package com.njpg.loomcore.viewmodel

import androidx.lifecycle.ViewModel
import com.njpg.loomcore.data.JsonRepository
import com.njpg.loomcore.data.Paths
import com.njpg.loomcore.model.Supplier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SuppliersViewModel : ViewModel() {

    private val repo = JsonRepository(
        file = Paths.suppliersFile, serializer = Supplier.serializer(), getId = { it.id })

    private val _suppliers = MutableStateFlow<List<Supplier>>(emptyList())
    val suppliers = _suppliers.asStateFlow()

    init {
        _suppliers.value = repo.loadAll()
    }

    fun add(supplier: Supplier) {
        _suppliers.update { it + supplier }
        repo.saveAll(_suppliers.value)
    }

    fun update(supplier: Supplier) {
        _suppliers.update { list -> list.map { if (it.id == supplier.id) supplier else it } }
        repo.saveAll(_suppliers.value)
    }

    fun delete(id: Int) {
        _suppliers.update { list -> list.filter { it.id != id } }
        repo.saveAll(_suppliers.value)
    }

    fun nextId(): Int = repo.nextId(_suppliers.value)
}