package com.njpg.loomcore.core

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.imageio.ImageIO

fun formatAsDate(date: String): String {
    var out = ""
    date.forEachIndexed { index, char ->
        out += char
        if ((index == 1 || index == 3) && index != date.lastIndex) {
            out += "."
        }
    }
    return out
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
        } else {
            null
        }
    }

    return bitmap
}