package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

/**
 * Одна строка прейскуранта — конкретная операция и её цена.
 *
 * @property id       Уникальный идентификатор строки внутри группы.
 * @property service  Название операции, например "Замена молнии".
 * @property price    Цена за единицу в виде строки, например "350.00".
 */
@Serializable
data class PriceRow(
    val id: Int,
    val service: String,
    val price: String
)