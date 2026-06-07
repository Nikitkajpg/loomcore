package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

@Serializable
data class PriceRow(
    val id: Int,
    val service: String,
    val price: String
)