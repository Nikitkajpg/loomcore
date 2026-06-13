package com.njpg.loomcore.ui.screen.tabs.products.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.njpg.loomcore.core.rememberAsyncImageBitmap
import com.njpg.loomcore.data.ImageStorage
import com.njpg.loomcore.model.Material
import com.njpg.loomcore.model.Product

/**
 * Развёрнутая часть карточки изделия — отображается при [isExpanded] == true.
 *
 * Анимирована через [AnimatedVisibility] для плавного появления/скрытия.
 * Разделена на две колонки:
 *
 * - **Левая** — текстовые детали: время пошива, материалы, заметки.
 * - **Правая** — горизонтальная галерея фотографий.
 *   При наведении на миниатюру показывается полноразмерное фото через
 *
 * @param product     Изделие, данные которого отображаются.
 * @param allMaterials Список всех материалов — для поиска названия по id.
 * @param isExpanded  Флаг видимости развёрнутой части.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExtendedCardPart(product: Product, allMaterials: List<Material>, isExpanded: Boolean) {
    AnimatedVisibility(visible = isExpanded) {
        Column(modifier = Modifier.padding(top = 8.dp)) {
            HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (product.workTimeHours > 0) {
                        LabeledRow("Время пошива") {
                            Text("${product.workTimeHours} ч.", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    if (product.materialsUsed.isNotEmpty()) {
                        LabeledRow("Материалы") {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                product.materialsUsed.forEach { usage ->
                                    val mat = allMaterials.find { it.id == usage.materialId }
                                    Text(
                                        "${mat?.name ?: "?"}: ${usage.amount} ${mat?.unitName ?: ""}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                    if (product.notes.isNotBlank()) {
                        LabeledRow("Заметки") {
                            Text(product.notes, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                Column(modifier = Modifier.weight(2f)) {
                    if (product.photoPaths.isNotEmpty()) {
                        Text(
                            "Фотографии",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(4.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth().height(250.dp)
                        ) {
                            items(product.photoPaths) { photoName ->
                                val file = remember(photoName) { ImageStorage.resolve(photoName)?.toFile() }
                                val bmp: ImageBitmap? = rememberAsyncImageBitmap(file)

                                TooltipArea(
                                    tooltip = {
                                        if (bmp != null) {
                                            Box(
                                                modifier = Modifier.sizeIn(maxWidth = 1000.dp, maxHeight = 1000.dp)
                                                    .background(
                                                        MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp)
                                                    ).padding(4.dp).clip(RoundedCornerShape(4.dp))
                                            ) {
                                                Image(
                                                    bmp,
                                                    "Увеличенное фото",
                                                    contentScale = ContentScale.Fit,
                                                    modifier = Modifier.wrapContentSize()
                                                )
                                            }
                                        }
                                    },
                                    delayMillis = 400,
                                    tooltipPlacement = TooltipPlacement.CursorPoint(alignment = Alignment.BottomEnd)
                                ) {
                                    Box(
                                        modifier = Modifier.size(250.dp).clip(RoundedCornerShape(8.dp))
                                            .background(MaterialTheme.colorScheme.surfaceVariant),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (bmp != null) {
                                            Image(
                                                bmp,
                                                null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        } else {
                                            Icon(
                                                Icons.Default.Photo,
                                                null,
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Text(
                            "Нет фотографий",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}