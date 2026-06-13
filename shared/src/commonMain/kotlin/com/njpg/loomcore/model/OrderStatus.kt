package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

/**
 * Статус выполнения заказа.
 */
@Serializable
enum class OrderStatus(val label: String) {
    IN_PROGRESS("В работе"),
    DONE("Завершён"),
    CANCELLED("Отменён")
}
