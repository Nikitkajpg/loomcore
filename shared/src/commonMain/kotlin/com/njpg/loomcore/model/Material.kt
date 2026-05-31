package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

@Serializable
data class Material(
    val id: Int,
    val name: String,
    val costPerUnit: Double,
    val unitName: String = "шт.",
    val supplierId: Int? = null,
    val notes: String = ""
)