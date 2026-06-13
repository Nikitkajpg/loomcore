package com.njpg.loomcore.viewmodel

import androidx.lifecycle.ViewModel
import com.njpg.loomcore.data.JsonRepository
import com.njpg.loomcore.data.Paths
import com.njpg.loomcore.model.Client
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel для раздела "Покупатели".
 *
 * Загружает список клиентов из JSON-файла при создании и реактивно
 * обновляет UI через [clients] StateFlow при каждом изменении.
 * После каждой мутации список автоматически сохраняется на диск.
 */
class ClientsViewModel : ViewModel() {

    private val repo = JsonRepository(
        file = Paths.clientsFile, serializer = Client.serializer(), getId = { it.id })

    private val _clients = MutableStateFlow<List<Client>>(emptyList())

    /** Реактивный список клиентов. */
    val clients = _clients.asStateFlow()

    init {
        _clients.value = repo.loadAll()
    }

    /** Добавляет нового клиента в конец списка и сохраняет. */
    fun add(client: Client) {
        _clients.update { it + client }
        repo.saveAll(_clients.value)
    }

    /** Обновляет существующего клиента (ищет по [Client.id]) и сохраняет. */
    fun update(client: Client) {
        _clients.update { list -> list.map { if (it.id == client.id) client else it } }
        repo.saveAll(_clients.value)
    }

    /** Удаляет клиента по [id] и сохраняет. */
    fun delete(id: Int) {
        _clients.update { list -> list.filter { it.id != id } }
        repo.saveAll(_clients.value)
    }

    /** Возвращает следующий свободный идентификатор. */
    fun nextId(): Int = repo.nextId(_clients.value)
}