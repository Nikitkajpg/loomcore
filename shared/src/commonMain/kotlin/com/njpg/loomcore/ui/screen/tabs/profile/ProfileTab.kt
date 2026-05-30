package com.njpg.loomcore.ui.screen.tabs.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.njpg.loomcore.model.Profile
import com.njpg.loomcore.viewmodel.ProfileViewModel

@Composable
fun ProfileTab(vm: ProfileViewModel) {
    val profile by vm.profile.collectAsState()

    var brandName by remember(profile) { mutableStateOf(profile.brandName) }
    var markupPercent by remember(profile) { mutableStateOf(profile.markupPercent.toString()) }
    var hourlyRate by remember(profile) { mutableStateOf(profile.hourlyRate.toString()) }
    var defaultCurrency by remember(profile) { mutableStateOf(profile.defaultCurrency) }
    var isDirty by remember { mutableStateOf(false) }

    fun markDirty() {
        isDirty = true
    }

    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Профиль производителя", style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(
                value = brandName,
                onValueChange = { brandName = it; markDirty() },
                label = { Text("Название / Имя") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = markupPercent,
                onValueChange = { markupPercent = it; markDirty() },
                label = { Text("Наценка (%)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                supportingText = { Text("Например: 50 = +50% к себестоимости") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = hourlyRate,
                onValueChange = { hourlyRate = it; markDirty() },
                label = { Text("Стоимость часа работы") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = defaultCurrency,
                onValueChange = { defaultCurrency = it; markDirty() },
                label = { Text("Валюта") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    vm.update(
                        Profile(
                            id = profile.id,
                            brandName = brandName.trim(),
                            markupPercent = markupPercent.toDoubleOrNull() ?: 0.0,
                            hourlyRate = hourlyRate.toDoubleOrNull() ?: 0.0,
                            defaultCurrency = defaultCurrency.trim().ifBlank { "Br" })
                    )
                    isDirty = false
                }, enabled = isDirty, modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сохранить")
            }
        }
    }
}