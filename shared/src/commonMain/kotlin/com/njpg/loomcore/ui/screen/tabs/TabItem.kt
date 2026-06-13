package com.njpg.loomcore.ui.screen.tabs

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Модель одной вкладки боковой навигации.
 *
 * @property label  Подпись под иконкой.
 * @property icon   Иконка Material Icons.
 */
data class TabItem(
    val label: String,
    val icon: ImageVector
)