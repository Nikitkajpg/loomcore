package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

/**
 * Запись о расходе конкретного материала в изделии или заказе.
 *
 * Хранит только ссылку на материал ([materialId]) и количество ([amount]),
 * а не само название и цену — они берутся из [Material] при расчётах.
 * Это позволяет обновить цену материала и пересчитать все заказы.
 *
 * @property materialId  ID материала из справочника [Material].
 * @property amount      Количество в единицах, указанных в [Material.unitName].
 */
@Serializable
data class MaterialUsage(
    val materialId: Int,
    val amount: Double
)