package com.njpg.loomcore.ui.screen.tabs.clients

import androidx.compose.runtime.Composable
import com.njpg.loomcore.model.Client
import com.njpg.loomcore.ui.screen.tabs.PartnerCard

/**
 * Карточка клиента в списке раздела "Покупатели".
 *
 * Делегирует отображение универсальному [PartnerCard],
 * передавая в него телефон, ссылку и заметки клиента.
 *
 * @param client    Отображаемый клиент.
 * @param onEdit    Открывает [ClientDialog] для редактирования.
 * @param onDelete  Открывает [ConfirmDeleteDialog] для удаления.
 */
@Composable
fun ClientCard(
    client: Client, onEdit: () -> Unit, onDelete: () -> Unit
) {
    PartnerCard(
        name = client.name,
        lines = listOf(client.phoneNumber, client.url, client.notes),
        onEdit = onEdit,
        onDelete = onDelete
    )
}