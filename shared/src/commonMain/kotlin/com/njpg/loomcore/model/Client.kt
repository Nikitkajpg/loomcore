package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

@Serializable
data class Client(
    val id: Int,
    val name: String,
    val phoneNumber: String = "",
    val url: String = "",
    val notes: String = ""
)