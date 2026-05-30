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
import com.njpg.loomcore.core.DateVisualTransformation
import com.njpg.loomcore.core.formatAsDate
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
    allClients: List<Client>,
    profile: Profile,
    onConfirm: (Product) -> Unit,
    onDismiss: () -> Unit
) {
    val productId = initial?.id ?: nextId

    var name by remember { mutableStateOf(initial?.name ?: "") }
    var notes by remember { mutableStateOf(initial?.notes ?: "") }
    var workTimeHours by remember { mutableStateOf(initial?.workTimeHours?.toString() ?: "") }
    var startDate by remember { mutableStateOf(initial?.startDate?.filter { it.isDigit() } ?: "") }
    var endDate by remember { mutableStateOf(initial?.endDate?.filter { it.isDigit() } ?: "") }

    val existingPhotos = remember { mutableStateListOf(*(initial?.photoPaths?.toTypedArray() ?: emptyArray())) }
    val newSelectedPhotos = remember { mutableStateListOf<File>() }

    val usageRows = remember {
        mutableStateListOf(
            *(initial?.materialsUsed?.map { it.materialId to it.amount.toString() }?.toTypedArray() ?: emptyArray())
        )
    }

    val selectedClientIds = remember { mutableStateSetOf(*(initial?.clientIds?.toTypedArray() ?: emptyArray())) }

    val previewCost by remember(usageRows.toList(), workTimeHours) {
        derivedStateOf {
            val matCost = usageRows.sumOf { (matId, amountStr) ->
                val mat = allMaterials.find { it.id == matId }
                (mat?.costPerUnit ?: 0.0) * (amountStr.toDoubleOrNull() ?: 0.0)
            }
            val labor = (workTimeHours.toDoubleOrNull() ?: 0.0) * profile.hourlyRate
            matCost + labor
        }
    }
    val previewPrice = previewCost * (1.0 + profile.markupPercent / 100.0)

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
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = startDate,
                            onValueChange = { v ->
                                val d = v.filter { it.isDigit() }
                                if (d.length <= 8) startDate = d
                            },
                            label = { Text("Дата начала") },
                            placeholder = { Text("ДД.ММ.ГГГГ") },
                            singleLine = true,
                            visualTransformation = DateVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = endDate,
                            onValueChange = { v ->
                                val d = v.filter { it.isDigit() }
                                if (d.length <= 8) endDate = d
                            },
                            label = { Text("Дата конца") },
                            placeholder = { Text("ДД.ММ.ГГГГ") },
                            singleLine = true,
                            visualTransformation = DateVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }
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

                if (allClients.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(4.dp))
                        Text("Покупатели", style = MaterialTheme.typography.labelLarge)
                    }
                    itemsIndexed(allClients) { _, client ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = client.id in selectedClientIds, onCheckedChange = { checked ->
                                    if (checked) selectedClientIds.add(client.id)
                                    else selectedClientIds.remove(client.id)
                                })
                            Text(client.name, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(4.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Себестоимость:", style = MaterialTheme.typography.bodySmall)
                        Text(
                            "%.2f ${profile.defaultCurrency}".format(previewCost),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Цена (+${profile.markupPercent}%):", style = MaterialTheme.typography.bodySmall)
                        Text(
                            "%.2f ${profile.defaultCurrency}".format(previewPrice),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
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
                val usages = usageRows.filter { (_, amt) -> amt.toDoubleOrNull() != null }
                    .map { (matId, amt) -> MaterialUsage(matId, amt.toDouble()) }

                val matCost = usages.sumOf { usage ->
                    val mat = allMaterials.find { it.id == usage.materialId }
                    (mat?.costPerUnit ?: 0.0) * usage.amount
                }
                val labor = (workTimeHours.toDoubleOrNull() ?: 0.0) * profile.hourlyRate
                val cachedCost = matCost + labor
                val finalPrice = cachedCost * (1.0 + profile.markupPercent / 100.0)

                val importedNewNames = newSelectedPhotos.map { ImageStorage.importPhoto(it.toPath()) }
                val finalPhotoPaths = existingPhotos + importedNewNames

                val removedPhotos = (initial?.photoPaths ?: emptyList()) - existingPhotos.toSet()
                removedPhotos.forEach { ImageStorage.deletePhoto(it) }

                onConfirm(
                    Product(
                        id = productId,
                        name = name.trim(),
                        materialsUsed = usages,
                        clientIds = selectedClientIds.toList(),
                        workTimeHours = workTimeHours.toDoubleOrNull() ?: 0.0,
                        startDate = formatAsDate(startDate).ifBlank { null },
                        endDate = formatAsDate(endDate).ifBlank { null },
                        cachedCost = cachedCost,
                        finalPrice = finalPrice,
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