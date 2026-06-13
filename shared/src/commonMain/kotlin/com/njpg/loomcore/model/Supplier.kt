package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

/**
 * Поставщик материалов.
 *
 * Привязывается к материалам через [Material.supplierId].
 * При удалении поставщика привязанные материалы получают предупреждение в UI.
 *
 * @property id    Уникальный идентификатор.
 * @property name  Название поставщика.
 * @property url   Ссылка на сайт или магазин поставщика.
 * @property notes Произвольные заметки (условия доставки, контакты и т.п.).
 */
@Serializable
data class Supplier(
    val id: Int,
    val name: String,
    val url: String = "",
    val notes: String = ""
)