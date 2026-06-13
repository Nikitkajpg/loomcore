package com.njpg.loomcore.viewmodel

import com.njpg.loomcore.data.Paths
import com.njpg.loomcore.model.Client

/**
 * ViewModel для раздела "Покупатели".
 */
class ClientsViewModel : BaseViewModel<Client>(
    file = Paths.clientsFile, serializer = Client.serializer()
) {
    val clients = items
}