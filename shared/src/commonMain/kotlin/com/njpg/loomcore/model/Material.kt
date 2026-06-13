package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

/**
 * Материал, используемый при пошиве изделий.
 *
 * @property id          Уникальный идентификатор.
 * @property name        Название материала.
 * @property costPerUnit Цена за одну единицу измерения.
 * @property unitName    Единица измерения.
 * @property supplierId  ID поставщика из [Supplier], или null если не указан.
 * @property notes       Произвольные заметки.
 */
@Serializable
data class Material(
    override val id: Int,
    val name: String,
    val costPerUnit: Double,
    val unitName: String = "шт.",
    val supplierId: Int? = null,
    val notes: String = ""
) : HasId