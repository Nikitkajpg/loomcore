package com.njpg.loomcore.viewmodel

import com.njpg.loomcore.data.ImageStorage
import com.njpg.loomcore.data.Paths
import com.njpg.loomcore.model.Product

/**
 * ViewModel для раздела "Изделия".
 *
 * Особенность: при удалении изделия автоматически удаляются
 * связанные фотографии из [ImageStorage], чтобы не накапливать
 * «осиротевшие» файлы в папке img.
 */
class ProductsViewModel : BaseViewModel<Product>(
    file = Paths.productsFile, serializer = Product.serializer()
) {
    val products = items

    override fun delete(id: Int) {
        items.value.find { it.id == id }?.photoPaths?.forEach { ImageStorage.deletePhoto(it) }

        super.delete(id)
    }
}