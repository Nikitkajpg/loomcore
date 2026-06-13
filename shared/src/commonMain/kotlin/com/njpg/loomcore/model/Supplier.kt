package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

/**
 * Поставщик материалов.
 *
 * @property id    Уникальный идентификатор.
 * @property name  Название поставщика.
 * @property url   Ссылка на сайт или магазин поставщика.
 * @property notes Произвольные заметки (условия доставки, контакты и т.п.).
 */
@Serializable
data class Supplier(
    override val id: Int,
    val name: String,
    val url: String = "",
    val notes: String = ""
) : HasId