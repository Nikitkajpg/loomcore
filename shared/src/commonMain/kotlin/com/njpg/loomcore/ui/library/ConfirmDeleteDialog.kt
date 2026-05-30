package com.njpg.loomcore.ui.library

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun ConfirmDeleteDialog(
    itemName: String,
    warningText: String? = null,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Удаление") },
        text = {
            Text(buildAnnotatedString {
                append("Вы действительно хотите удалить \"")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(itemName) }
                append("\"?\n")

                if (!warningText.isNullOrBlank()) {
                    append("\n")
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Medium)) {
                        append("ВНИМАНИЕ:\n")
                    }
                    append(warningText)
                    append("\n")
                }

                append("\nЭто действие нельзя отменить.")
            })
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Удалить", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}