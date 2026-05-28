package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductUnit(
    val id: Int,
    val number: String,
    val name: String,
    val cost: Double,
    val price: Double,
    val photoPath: String? = null
)