package com.njpg.loomcore.core

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * [VisualTransformation], которая превращает строку из цифр (до 8 символов)
 * в отформатированную дату вида ДД.ММ.ГГГГ прямо в текстовом поле.
 *
 * Используется вместе с [formatAsDate] для единообразного отображения дат
 * во всех диалогах ввода заказов.
 */
class DateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val out = formatAsDate(text.text)

        /**
         * Маппинг позиций: при вставке точек позиции сдвигаются.
         * Логика:
         *   - до 3-го символа → позиция не меняется
         *   - от 3-го до 5-го → смещение +1 (одна точка вставлена)
         *   - от 5-го → смещение +2 (две точки вставлены)
         */
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = when {
                offset <= 2 -> offset
                offset <= 4 -> offset + 1
                else -> offset + 2
            }

            override fun transformedToOriginal(offset: Int): Int = when {
                offset <= 2 -> offset
                offset <= 5 -> offset - 1
                else -> offset - 2
            }
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}