package com.njpg.loomcore.viewmodel

import androidx.lifecycle.ViewModel
import com.njpg.loomcore.data.JsonRepository
import com.njpg.loomcore.data.Paths
import com.njpg.loomcore.model.Material
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel для раздела "Материалы".
 *
 * Реактивно предоставляет список материалов через [materials].
 * Передаётся как в [MaterialsTab], так и в [OrderDialog] для
 * расчёта себестоимости заказов.
 */
class MaterialsViewModel : ViewModel() {

    private val repo = JsonRepository(
        file = Paths.materialsFile, serializer = Material.serializer(), getId = { it.id })

    private val _materials = MutableStateFlow<List<Material>>(emptyList())

    /** Реактивный список материалов. */
    val materials = _materials.asStateFlow()

    init {
        _materials.value = repo.loadAll()
    }

    /** Добавляет новый материал в конец списка и сохраняет. */
    fun add(material: Material) {
        _materials.update { it + material }
        repo.saveAll(_materials.value)
    }

    /** Обновляет существующий материал (ищет по [Material.id]) и сохраняет. */
    fun update(material: Material) {
        _materials.update { list -> list.map { if (it.id == material.id) material else it } }
        repo.saveAll(_materials.value)
    }

    /** Удаляет материал по [id] и сохраняет. */
    fun delete(id: Int) {
        _materials.update { list -> list.filter { it.id != id } }
        repo.saveAll(_materials.value)
    }

    /** Возвращает следующий свободный идентификатор. */
    fun nextId(): Int = repo.nextId(_materials.value)
}