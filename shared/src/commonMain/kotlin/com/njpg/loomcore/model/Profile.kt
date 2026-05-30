package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: Int = 1,
    val brandName: String = "",
    /** Наценка */
    val markupPercent: Double = 0.0,
    /** Стоимость часа работы */
    val hourlyRate: Double = 0.0,
    val defaultCurrency: String = "Br",
)