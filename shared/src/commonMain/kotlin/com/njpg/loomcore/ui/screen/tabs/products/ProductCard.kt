package com.njpg.loomcore.ui.screen.tabs.products

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
import com.njpg.loomcore.model.Client
import com.njpg.loomcore.model.Material
import com.njpg.loomcore.model.Product
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import javax.imageio.ImageIO

@Composable
fun ProductCard(
    product: Product,
    allMaterials: List<Material>,
    allClients: List<Client>,
    currency: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onPickPhoto: (File) -> Unit
) {
    val bitmap: ImageBitmap? = remember(product.photoPath) {
        ImageStorage.resolve(product.photoPath)?.let {
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
            verticalAlignment = Alignment.Top
        ) {

            Box(
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp))
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
                    Icon(Icons.Default.Photo, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(product.name, style = MaterialTheme.typography.titleMedium)

                if (product.startDate != null || product.endDate != null) {
                    Text(
                        text = buildString {
                            if (product.startDate != null) append(product.startDate)
                            if (product.startDate != null && product.endDate != null) append(" — ")
                            if (product.endDate != null) append(product.endDate)
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (product.materialsUsed.isNotEmpty()) {
                    Text(
                        text = product.materialsUsed.joinToString(", ") { usage ->
                            val mat = allMaterials.find { it.id == usage.materialId }
                            "${mat?.name ?: "?"}: ${usage.amount} ${mat?.unitName ?: ""}"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (product.clientIds.isNotEmpty()) {
                    val names = product.clientIds.mapNotNull { id -> allClients.find { it.id == id }?.name }
                    Text(
                        text = names.joinToString(", "),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.height(2.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column {
                        Text(
                            "Себестоимость",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "%.2f %s".format(product.cachedCost, currency), style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Column {
                        Text(
                            "Цена",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "%.2f %s".format(product.finalPrice, currency),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                if (product.notes.isNotBlank()) {
                    Text(
                        product.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = {
                    val fd = FileDialog(null as Frame?, "Выбрать фото", FileDialog.LOAD)
                    fd.file = "*.jpg;*.jpeg;*.png;*.webp"
                    fd.isVisible = true
                    if (fd.file != null) onPickPhoto(File(fd.directory, fd.file))
                }) { Icon(Icons.Default.Photo, contentDescription = "Фото") }

                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Редактировать")
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete, contentDescription = "Удалить", tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}