package com.njpg.loomcore.ui.screen.tabs.products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.njpg.loomcore.data.ImageStorage
import com.njpg.loomcore.model.*
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
fun ProductDialog(
    initial: Product?,
    nextId: Int,
    allMaterials: List<Material>,
    onConfirm: (Product) -> Unit,
    onDismiss: () -> Unit
) {
    val productId = initial?.id ?: nextId

    var name by remember { mutableStateOf(initial?.name ?: "") }
    var notes by remember { mutableStateOf(initial?.notes ?: "") }
    var workTimeHours by remember { mutableStateOf(initial?.workTimeHours?.toString() ?: "") }

    val existingPhotos = remember { mutableStateListOf(*(initial?.photoPaths?.toTypedArray() ?: emptyArray())) }
    val newSelectedPhotos = remember { mutableStateListOf<File>() }

    val usageRows = remember {
        mutableStateListOf(
            *(initial?.materialsUsed?.map { it.materialId to it.amount.toString() }?.toTypedArray() ?: emptyArray())
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Новое изделие" else "Редактировать изделие") },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().heightIn(max = 500.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Название изделия") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = workTimeHours,
                        onValueChange = { workTimeHours = it },
                        label = { Text("Время пошива (часы)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Spacer(Modifier.height(4.dp))
                    Text("Материалы", style = MaterialTheme.typography.labelLarge)
                }
                itemsIndexed(usageRows) { index, (matId, amountStr) ->
                    MaterialUsageRow(
                        materialId = matId,
                        amount = amountStr,
                        allMaterials = allMaterials,
                        onMaterialChange = { newId -> usageRows[index] = newId to amountStr },
                        onAmountChange = { newAmt -> usageRows[index] = matId to newAmt },
                        onDelete = { usageRows.removeAt(index) })
                }
                item {
                    TextButton(
                        onClick = {
                            val firstId = allMaterials.firstOrNull()?.id ?: return@TextButton
                            usageRows.add(firstId to "1")
                        }, enabled = allMaterials.isNotEmpty()
                    ) {
                        Icon(Icons.Default.Add, null)
                        Spacer(Modifier.width(4.dp))
                        Text("Добавить материал")
                    }
                }

                item {
                    Spacer(Modifier.height(8.dp))
                    Text("Фотографии", style = MaterialTheme.typography.labelLarge)
                }
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth().height(100.dp)
                    ) {
                        item {
                            Box(
                                modifier = Modifier.size(100.dp).clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant).clickable {
                                        val fd = FileDialog(null as Frame?, "Выбрать фото", FileDialog.LOAD)
                                        fd.file = "*.jpg;*.jpeg;*.png;*.webp"
                                        fd.isMultipleMode = true
                                        fd.isVisible = true
                                        if (fd.files != null) newSelectedPhotos.addAll(fd.files)
                                    }, contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    "Добавить фото",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        items(existingPhotos) { photoName ->
                            PhotoPreviewItem(
                                file = ImageStorage.resolve(photoName)?.toFile(),
                                onDelete = { existingPhotos.remove(photoName) })
                        }
                        items(newSelectedPhotos) { file ->
                            PhotoPreviewItem(
                                file = file, onDelete = { newSelectedPhotos.remove(file) })
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Заметки") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val usages = usageRows.filter { (_, amt) -> amt.replace(',', '.').toDoubleOrNull() != null }
                    .map { (matId, amt) -> MaterialUsage(matId, amt.replace(',', '.').toDouble()) }

                val importedNewNames = newSelectedPhotos.map { ImageStorage.importPhoto(it.toPath()) }
                val finalPhotoPaths = existingPhotos + importedNewNames

                val removedPhotos = (initial?.photoPaths ?: emptyList()) - existingPhotos.toSet()
                removedPhotos.forEach { ImageStorage.deletePhoto(it) }

                onConfirm(
                    Product(
                        id = productId,
                        name = name.trim(),
                        materialsUsed = usages,
                        workTimeHours = workTimeHours.replace(',', '.').toDoubleOrNull() ?: 0.0,
                        photoPaths = finalPhotoPaths,
                        notes = notes.trim()
                    )
                )
            }) { Text("Сохранить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        })
}