package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

@Serializable
data class Supplier(
    val id: Int, val name: String, val url: String = "", val notes: String = ""
)