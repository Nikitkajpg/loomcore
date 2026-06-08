package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val materialsUsed: List<MaterialUsage> = emptyList(),
    val workTimeHours: Double = 0.0,
    val photoPaths: List<String> = emptyList(),
    val notes: String = ""
)