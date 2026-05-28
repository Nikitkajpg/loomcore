package com.njpg.loomcore.ui.components

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


@Composable
fun ContactDialog(
    title: String, initial: ContactDraft?, nextId: Int, onConfirm: (ContactDraft) -> Unit, onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(initial?.name ?: "") }
    var contact by remember { mutableStateOf(initial?.contact ?: "") }
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
                value = contact,
                onValueChange = { contact = it },
                label = { Text("Контакт") },
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
            onConfirm(ContactDraft(initial?.id ?: nextId, name.trim(), contact.trim(), notes.trim()))
        }) { Text("Сохранить") }
    }, dismissButton = {
        TextButton(onClick = onDismiss) { Text("Отмена") }
    })
}