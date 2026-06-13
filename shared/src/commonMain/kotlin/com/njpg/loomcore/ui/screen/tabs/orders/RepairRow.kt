package com.njpg.loomcore.ui.screen.tabs.orders

/**
 * Временная строка операции ремонта — UI-модель внутри [OrderDialog].
 *
 * Используется только во время редактирования в диалоге и не сохраняется
 * напрямую. При подтверждении диалога конвертируется в [RepairOperation].
 *
 * @property priceRowId  Id строки прейскуранта ([PriceRow.id]), если операция
 *                       выбрана из прейскуранта. `null` при ручном вводе.
 * @property name        Название операции.
 * @property quantity    Количество в строковом формате.
 * @property unitPrice   Цена за единицу в строковом формате.
 */
data class RepairRow(
    val priceRowId: Int?,
    val name: String,
    val quantity: String,
    val unitPrice: String,
)