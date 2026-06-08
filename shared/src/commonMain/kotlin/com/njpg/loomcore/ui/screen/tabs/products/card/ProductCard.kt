package com.njpg.loomcore.ui.screen.tabs.products.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

@Composable
fun ProductCard(product: Product, allMaterials: List<Material>, onEdit: () -> Unit, onDelete: () -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    val previewFile = remember(product.photoPaths) { ImageStorage.resolve(product.photoPaths.firstOrNull())?.toFile() }
    val previewBitmap: ImageBitmap? = rememberAsyncImageBitmap(previewFile)

    Card(
        modifier = Modifier.fillMaxWidth().clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(64.dp).clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center
                ) {
                    if (previewBitmap != null) {
                        Image(previewBitmap, null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                    } else {
                        Icon(Icons.Default.Photo, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(product.name, style = MaterialTheme.typography.titleMedium)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, "Редактировать") }
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete, "Удалить", tint = MaterialTheme.colorScheme.error
                        )
                    }
                    Icon(
                        if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            ExtendedCardPart(product, allMaterials, isExpanded)
        }
    }
}