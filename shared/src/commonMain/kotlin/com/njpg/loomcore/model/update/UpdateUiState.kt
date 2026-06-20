package com.njpg.loomcore.model.update

data class UpdateUiState(
    val isChecking: Boolean = false,
    val availableUpdate: UpdateInfo? = null,
    val errorMessage: String? = null,
    val dismissed: Boolean = false
)