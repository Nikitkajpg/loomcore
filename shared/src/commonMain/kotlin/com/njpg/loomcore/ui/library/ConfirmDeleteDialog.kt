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

/**
 * Универсальный диалог подтверждения удаления.
 *
 * Используется во всех разделах приложения перед необратимым удалением записи.
 * Опционально показывает предупреждение [warningText] красным цветом — например,
 * при удалении поставщика, к которому привязаны материалы.
 *
 * @param itemName     Название удаляемого объекта.
 * @param warningText  Дополнительное предупреждение об удалении связанных данных.
 *                     Если null или пустое — не отображается.
 * @param onConfirm    Вызывается при нажатии "Удалить".
 * @param onDismiss    Вызывается при нажатии "Отмена" или закрытии диалога.
 */
@Composable
fun ConfirmDeleteDialog(
    itemName: String, warningText: String? = null, onConfirm: () -> Unit, onDismiss: () -> Unit
) {
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Удаление") }, text = {
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
    }, confirmButton = {
        TextButton(onClick = onConfirm) {
            Text("Удалить", color = MaterialTheme.colorScheme.error)
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) { Text("Отмена") }
    })
}