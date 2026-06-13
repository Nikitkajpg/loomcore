package com.njpg.loomcore.viewmodel

import androidx.lifecycle.ViewModel
import com.njpg.loomcore.data.JsonRepository
import com.njpg.loomcore.model.HasId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.KSerializer
import java.nio.file.Path

/**
 * Базовый ViewModel для разделов с плоским списком сущностей.
 *
 * Инкапсулирует стандартный CRUD-паттерн: загрузка из JSON при старте,
 * реактивный StateFlow для UI, сохранение на диск после каждой мутации.
 *
 * Наследники передают в конструктор только специфику своего типа:
 * файл хранилища и сериализатор. Сущность должна реализовывать интерфейс [HasId].
 *
 * @param T          Тип сущности, реализующий [HasId].
 * @param file       Путь к JSON-файлу хранилища.
 * @param serializer KSerializer для типа T.
 */
abstract class BaseViewModel<T : HasId>(
    file: Path,
    serializer: KSerializer<T>,
) : ViewModel() {

    private val repo = JsonRepository(
        file = file, serializer = serializer, getId = { it.id })

    protected val _items = MutableStateFlow<List<T>>(emptyList())
    val items = _items.asStateFlow()

    init {
        _items.value = repo.loadAll()
    }

    fun add(item: T) {
        _items.update { it + item }
        repo.saveAll(_items.value)
    }

    fun update(item: T) {
        _items.update { list ->
            list.map { if (it.id == item.id) item else it }
        }
        repo.saveAll(_items.value)
    }

    open fun delete(id: Int) {
        _items.update { list -> list.filter { it.id != id } }
        repo.saveAll(_items.value)
    }

    fun nextId(): Int = repo.nextId(_items.value)
}