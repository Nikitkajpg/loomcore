package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

@Serializable
enum class OrderType(val label: String) {
    PRODUCT("Заказ"),
    REPAIR("Ремонт")
}
