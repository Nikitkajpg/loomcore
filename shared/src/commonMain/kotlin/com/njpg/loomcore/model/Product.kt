package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val materialsUsed: List<MaterialUsage> = emptyList(),
    val clientIds: List<Int> = emptyList(),
    val workTimeHours: Double = 0.0,
    val startDate: String? = null,
    val endDate: String? = null,
    /** Себестоимость */
    val cachedCost: Double = 0.0,
    /** Итоговая цена */
    val finalPrice: Double = 0.0,
    val photoPath: String? = null,
    val notes: String = ""
)