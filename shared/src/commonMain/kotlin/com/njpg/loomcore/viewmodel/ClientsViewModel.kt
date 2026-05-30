package com.njpg.loomcore.viewmodel

import androidx.lifecycle.ViewModel
import com.njpg.loomcore.data.JsonRepository
import com.njpg.loomcore.data.Paths
import com.njpg.loomcore.model.Client
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ClientsViewModel : ViewModel() {

    private val repo = JsonRepository(
        Paths.clientsFile, Client.serializer(), getId = { it.id })

    private val _clients = MutableStateFlow<List<Client>>(emptyList())
    val clients = _clients.asStateFlow()

    init {
        _clients.value = repo.loadAll()
    }

    fun add(client: Client) {
        _clients.update { it + client }
        repo.saveAll(_clients.value)
    }

    fun update(client: Client) {
        _clients.update { list -> list.map { if (it.id == client.id) client else it } }
        repo.saveAll(_clients.value)
    }

    fun delete(id: Int) {
        _clients.update { list -> list.filter { it.id != id } }
        repo.saveAll(_clients.value)
    }

    fun nextId(): Int = repo.nextId(_clients.value)
}