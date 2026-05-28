package com.njpg.loomcore.ui.screen.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.njpg.loomcore.data.ImageStorage
import com.njpg.loomcore.model.ProductUnit
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import javax.imageio.ImageIO

@Composable
fun UnitCard(
    productUnit: ProductUnit, onEdit: (ProductUnit) -> kotlin.Unit, onDelete: (ProductUnit) -> kotlin.Unit, onPickPhoto: (File) -> kotlin.Unit
) {
    val bitmap: ImageBitmap? = remember(productUnit.photoPath) {
        ImageStorage.resolve(productUnit.photoPath)?.let {
            try {
                ImageIO.read(it.toFile())?.toComposeImageBitmap()
            } catch (_: Exception) {
                null
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(72.dp).clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center
            ) {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap,
                        contentDescription = "Фото изделия",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        Icons.Default.Photo,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    "№ ${productUnit.number}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(productUnit.name, style = MaterialTheme.typography.titleMedium)
                Text("Затраты: ${productUnit.cost}  •  Цена: ${productUnit.price}", style = MaterialTheme.typography.bodySmall)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = {
                    val fd = FileDialog(null as Frame?, "Выбрать фото", FileDialog.LOAD)
                    fd.file = "*.jpg;*.jpeg;*.png;*.webp"
                    fd.isVisible = true
                    if (fd.file != null) onPickPhoto(File(fd.directory, fd.file))
                }) { Icon(Icons.Default.Photo, contentDescription = "Фото") }

                IconButton(onClick = { onEdit(productUnit) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Редактировать")
                }
                IconButton(onClick = { onDelete(productUnit) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}