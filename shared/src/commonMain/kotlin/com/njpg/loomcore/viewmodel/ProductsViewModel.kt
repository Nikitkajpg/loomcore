package com.njpg.loomcore.viewmodel

import androidx.lifecycle.ViewModel
import com.njpg.loomcore.data.ImageStorage
import com.njpg.loomcore.data.JsonRepository
import com.njpg.loomcore.data.Paths
import com.njpg.loomcore.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.nio.file.Path

class ProductsViewModel : ViewModel() {

    private val repo = JsonRepository(
        Paths.productsFile, Product.serializer(), getId = { it.id })

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    init {
        _products.value = repo.loadAll()
    }

    fun add(unit: Product) {
        _products.update { it + unit }
        repo.saveAll(_products.value)
    }

    fun update(unit: Product) {
        _products.update { list -> list.map { if (it.id == unit.id) unit else it } }
        repo.saveAll(_products.value)
    }

    fun delete(id: Int) {
        _products.update { it.filter { u -> u.id != id } }
        repo.saveAll(_products.value)
    }

    fun importPhoto(unitId: Int, source: Path) {
        val fileName = ImageStorage.importPhoto(source)
        val unit = _products.value.find { it.id == unitId } ?: return
        update(unit.copy(photoPath = fileName))
    }

    fun nextId(): Int = repo.nextId(_products.value)
}