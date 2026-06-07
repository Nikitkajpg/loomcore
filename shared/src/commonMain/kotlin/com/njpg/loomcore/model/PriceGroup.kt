package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

@Serializable
data class PriceGroup(
    val id: Int,
    val name: String,
    val rows: List<PriceRow> = emptyList()
)