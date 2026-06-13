package com.njpg.loomcore.viewmodel

import com.njpg.loomcore.data.Paths
import com.njpg.loomcore.model.Supplier

/**
 * ViewModel для раздела "Поставщики".
 */
class SuppliersViewModel : BaseViewModel<Supplier>(
    file = Paths.suppliersFile, serializer = Supplier.serializer()
) {
    val suppliers = items
}