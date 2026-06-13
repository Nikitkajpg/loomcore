package com.njpg.loomcore.ui.screen.tabs.clients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.njpg.loomcore.model.Client

/**
 * Диалог создания и редактирования клиента.
 *
 * При создании [initial] = null, при редактировании — содержит
 * текущие данные клиента. Поле [nextId] используется только при создании.
 *
 * @param title      Заголовок диалога.
 * @param initial    Текущие данные для режима редактирования, null для создания.
 * @param nextId     Идентификатор для нового клиента (от [ClientsViewModel.nextId]).
 * @param onConfirm  Вызывается при нажатии "Сохранить" с готовым объектом [Client].
 * @param onDismiss  Вызывается при отмене или закрытии диалога.
 */
@Composable
fun ClientDialog(
    title: String, initial: Client?, nextId: Int, onConfirm: (Client) -> Unit, onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(initial?.name ?: "") }
    var phoneNumber by remember { mutableStateOf(initial?.phoneNumber ?: "") }
    var url by remember { mutableStateOf(initial?.url ?: "") }
    var notes by remember { mutableStateOf(initial?.notes ?: "") }

    AlertDialog(onDismissRequest = onDismiss, title = { Text(title) }, text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Название / Имя") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Номер телефона") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                label = { Text("Ссылка") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Заметки") },
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }, confirmButton = {
        TextButton(onClick = {
            onConfirm(
                Client(
                    id = initial?.id ?: nextId,
                    name = name.trim(),
                    phoneNumber = phoneNumber.trim(),
                    url = url.trim(),
                    notes = notes.trim()
                )
            )
        }) { Text("Сохранить") }
    }, dismissButton = {
        TextButton(onClick = onDismiss) { Text("Отмена") }
    })
}