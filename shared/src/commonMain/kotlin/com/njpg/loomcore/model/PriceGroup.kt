package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

/**
 * Группа операций в прейскуранте ремонтных услуг.
 *
 * Прейскурант организован в виде групп (например, "Брюки", "Верхняя одежда"),
 * каждая из которых содержит список конкретных операций [rows].
 *
 * @property id    Уникальный идентификатор группы.
 * @property name  Название группы (отображается как заголовок в дропдауне).
 * @property rows  Список строк-операций этой группы.
 */
@Serializable
data class PriceGroup(
    val id: Int,
    val name: String,
    val rows: List<PriceRow> = emptyList()
)