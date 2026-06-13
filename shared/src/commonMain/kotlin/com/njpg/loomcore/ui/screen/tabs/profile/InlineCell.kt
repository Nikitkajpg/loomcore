package com.njpg.loomcore.ui.screen.tabs.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

/**
 * Минималистичное редактируемое поле ввода для ячейки таблицы прейскуранта.
 *
 * @param value          Текущее значение поля.
 * @param onValueChange  Вызывается при каждом изменении текста.
 * @param modifier       Модификатор размера и отступов (задаётся снаружи).
 */
@Composable
fun InlineCell(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface, fontSize = MaterialTheme.typography.bodySmall.fontSize
        ),
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        modifier = modifier.padding(vertical = 6.dp),
        decorationBox = { inner -> Box(modifier = Modifier.fillMaxWidth()) { inner() } })
}