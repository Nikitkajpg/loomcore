package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

@Serializable
data class MaterialUsage(
    val materialId: Int, val amount: Double
)