package com.njpg.loomcore.core

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.imageio.ImageIO

fun formatAsDate(raw: String?): String {
    if (raw.isNullOrEmpty()) return ""
    val digits = raw.filter { it.isDigit() }.take(8)
    return buildString {
        digits.forEachIndexed { i, c ->
            append(c)
            if ((i == 1 || i == 3) && i != digits.lastIndex) append('.')
        }
    }
}

@Composable
fun rememberAsyncImageBitmap(file: File?): ImageBitmap? {
    var bitmap by remember(file) { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(file) {
        bitmap = if (file?.exists() == true) {
            withContext(Dispatchers.IO) {
                try {
                    ImageIO.read(file)?.toComposeImageBitmap()
                } catch (_: Exception) {
                    null
                }
            }
        } else null
    }
    return bitmap
}