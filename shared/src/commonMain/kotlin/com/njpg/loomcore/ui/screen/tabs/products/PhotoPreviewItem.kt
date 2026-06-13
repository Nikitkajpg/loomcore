package com.njpg.loomcore.ui.screen.tabs.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import java.io.File
import javax.imageio.ImageIO

/**
 * Миниатюра фотографии с кнопкой удаления в строке [LazyRow] в [ProductDialog].
 *
 * @param file      Файл изображения. Если `null` или не существует — показывает заглушку.
 * @param onDelete  Вызывается при нажатии кнопки "х" — удаляет эту миниатюру из списка.
 */
@Composable
fun PhotoPreviewItem(file: File?, onDelete: () -> Unit) {
    val bitmap: ImageBitmap? = remember(file) {
        if (file?.exists() == true) {
            try {
                ImageIO.read(file).toComposeImageBitmap()
            } catch (_: Exception) {
                null
            }
        } else null
    }

    Box(
        modifier = Modifier.size(100.dp).clip(RoundedCornerShape(8.dp))
    ) {
        if (bitmap != null) {
            Image(bitmap, null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        } else {
            Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.errorContainer))
        }

        IconButton(
            onClick = onDelete,
            modifier = Modifier.align(Alignment.TopEnd).size(24.dp)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
        ) {
            Icon(Icons.Default.Close, "Удалить", modifier = Modifier.size(16.dp))
        }
    }
}