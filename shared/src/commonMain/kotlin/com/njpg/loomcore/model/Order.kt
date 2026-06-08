package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val id: Int,
    val type: OrderType = OrderType.PRODUCT,
    val clientId: Int? = null,
    val status: OrderStatus = OrderStatus.IN_PROGRESS,
    val startDate: String = "",
    val endDate: String? = null,
    val productId: Int? = null,
    val materialsUsed: List<MaterialUsage> = emptyList(),
    val workTimeHours: Double = 0.0,
    val photoPaths: List<String> = emptyList(),
    val cachedCost: Double = 0.0,
    val finalPrice: Double = 0.0,
    val repairOperations: List<RepairOperation> = emptyList(),
    val repairTotalPrice: Double = 0.0,
    val notes: String = ""
)
