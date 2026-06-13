package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

/**
 * Запись о расходе конкретного материала в изделии или заказе.
 *
 * @property materialId  ID материала из справочника [Material].
 * @property amount      Количество в единицах, указанных в [Material.unitName].
 */
@Serializable
data class MaterialUsage(
    val materialId: Int,
    val amount: Double
)