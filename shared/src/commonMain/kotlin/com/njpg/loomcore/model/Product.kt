package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val cost: Double,
    val price: Double,
    val photoPath: String? = null
)