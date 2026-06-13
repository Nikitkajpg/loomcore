package com.njpg.loomcore.viewmodel

import com.njpg.loomcore.data.Paths
import com.njpg.loomcore.model.Material

/**
 * ViewModel для раздела "Материалы".
 */
class MaterialsViewModel : BaseViewModel<Material>(
    file = Paths.materialsFile, serializer = Material.serializer()
) {
    val materials = items
}