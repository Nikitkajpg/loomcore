package com.njpg.loomcore.update

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.njpg.loomcore.model.update.UpdateInfo
import java.awt.Desktop
import java.net.URI

@Composable
fun UpdateAvailableDialog(
    update: UpdateInfo, onDismiss: () -> Unit
) {
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Доступна новая версия") }, text = {
        Text(
            "Текущая версия: ${update.currentVersion}\n" + "Новая версия:   ${update.latestVersion.removePrefix("v")}\n\n" + (update.releaseNotes?.take(
            300
        )?.let { "$it…\n\n" } ?: ""))
    }, confirmButton = {
        TextButton(
            onClick = {
                runCatching {
                    Desktop.getDesktop().browse(URI(update.releaseUrl))
                }
                onDismiss()
            }) {
            Text("Открыть релиз")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Позже")
        }
    })
}