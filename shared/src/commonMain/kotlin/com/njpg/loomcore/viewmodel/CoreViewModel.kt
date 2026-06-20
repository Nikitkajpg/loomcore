package com.njpg.loomcore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.njpg.loomcore.core.UpdateChecker
import com.njpg.loomcore.model.update.UpdateUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CoreViewModel : ViewModel() {
    private val updateChecker = UpdateChecker()

    private val _updateState = MutableStateFlow(UpdateUiState())
    val updateState: StateFlow<UpdateUiState> = _updateState.asStateFlow()

    init {
        checkForUpdates()
    }

    fun checkForUpdates() {
        viewModelScope.launch {
            _updateState.update { it.copy(isChecking = true, errorMessage = null) }
            val result = updateChecker.checkForUpdate()
            _updateState.update {
                result.fold(
                    onSuccess = { update -> it.copy(isChecking = false, availableUpdate = update) },
                    onFailure = { err -> it.copy(isChecking = false, errorMessage = err.message) })
            }
        }
    }

    fun dismissUpdate() {
        _updateState.update { it.copy(dismissed = true) }
    }
}