package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

@Serializable
data class RepairOperation(
    val name: String = "",
    val quantity: Double = 1.0,
    val priceRowId: Int? = null,
    val unitPrice: Double = 0.0
) {
    val totalPrice: Double get() = unitPrice * quantity
}
