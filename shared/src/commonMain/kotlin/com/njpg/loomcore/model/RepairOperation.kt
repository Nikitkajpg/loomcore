package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

/**
 * Одна операция в заказе на ремонт.
 *
 * При выборе операции из прейскуранта ([PriceRow]) название и цена
 * копируются сюда как снапшот. Это гарантирует, что изменение прейскуранта
 * впоследствии не затронет уже сохранённые заказы.
 *
 * @property name        Название операции.
 * @property quantity    Количество повторений операции (по умолчанию 1).
 * @property priceRowId  ID строки [PriceRow], из которой взята операция, или null.
 * @property unitPrice   Цена за одно выполнение операции (снапшот из [PriceRow.price]).
 * @property totalPrice  Итоговая стоимость: [unitPrice] × [quantity] (вычисляемое свойство).
 */
@Serializable
data class RepairOperation(
    val name: String = "",
    val quantity: Double = 1.0,
    val priceRowId: Int? = null,
    val unitPrice: Double = 0.0
) {
    val totalPrice: Double get() = unitPrice * quantity
}
