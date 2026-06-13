package com.njpg.loomcore.core

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.imageio.ImageIO

/**
 * Форматирует строку из цифр в дату вида ДД.ММ.ГГГГ.
 * После 2-го и 4-го символа автоматически вставляет точку.
 *
 * Используется как в [DateVisualTransformation] (визуальный слой),
 * так и при сохранении итогового значения в модель.
 *
 * @param raw  Строка из цифр, например "01032025".
 * @return     Отформатированная строка, например "01.03.2025".
 *             При пустом или null-входе возвращает пустую строку.
 */
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

/**
 * Composable-хелпер для асинхронной загрузки изображения из файла.
 *
 * Читает файл в фоновом потоке (IO-dispatcher) через [ImageIO],
 * конвертирует в [ImageBitmap] и возвращает его через состояние.
 * При изменении [file] загрузка перезапускается автоматически.
 *
 * @param file  Файл изображения на диске.
 * @return      [ImageBitmap] если файл успешно прочитан, иначе null.
 */
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