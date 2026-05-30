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
        file = Paths.productsFile, serializer = Product.serializer(), getId = { it.id })

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    init {
        _products.value = repo.loadAll()
    }

    fun add(product: Product) {
        _products.update { it + product }
        repo.saveAll(_products.value)
    }

    fun update(product: Product) {
        _products.update { list -> list.map { if (it.id == product.id) product else it } }
        repo.saveAll(_products.value)
    }

    fun delete(id: Int) {
        _products.update { list -> list.filter { it.id != id } }
        repo.saveAll(_products.value)
    }

    fun importPhoto(productId: Int, source: Path) {
        val product = _products.value.find { it.id == productId } ?: return
        val fileName = ImageStorage.importPhoto(source)
        update(product.copy(photoPath = fileName))
    }

    fun nextId(): Int = repo.nextId(_products.value)
}