package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

/**
 * Тип заказа — определяет, какой набор полей [Order] используется.
 */
@Serializable
enum class OrderType(val label: String) {
    PRODUCT("Заказ"),
    REPAIR("Ремонт")
}
