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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.njpg.loomcore.model.Profile
import com.njpg.loomcore.viewmodel.PriceListViewModel
import com.njpg.loomcore.viewmodel.ProfileViewModel
import kotlinx.coroutines.delay

/**
 * Вкладка "Профиль" — настройки производителя и прейскурант услуг.
 *
 * @param vm  ViewModel профиля.
 */
@Composable
fun ProfileTab(vm: ProfileViewModel) {
    val profile by vm.profile.collectAsState()
    val priceVm: PriceListViewModel = viewModel { PriceListViewModel() }

    var brandName by remember(profile) { mutableStateOf(profile.brandName) }
    var markupPercent by remember(profile) { mutableStateOf(profile.markupPercent.toString()) }
    var hourlyRate by remember(profile) { mutableStateOf(profile.hourlyRate.toString()) }
    var defaultCurrency by remember(profile) { mutableStateOf(profile.defaultCurrency) }

    LaunchedEffect(brandName, markupPercent, hourlyRate, defaultCurrency) {
        delay(500)
        vm.update(
            Profile(
                id = profile.id,
                brandName = brandName.trim(),
                markupPercent = markupPercent.replace(',', '.').toDoubleOrNull() ?: profile.markupPercent,
                hourlyRate = hourlyRate.replace(',', '.').toDoubleOrNull() ?: profile.hourlyRate,
                defaultCurrency = defaultCurrency.trim().ifBlank { "Br" })
        )
    }

    Surface {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Профиль производителя", style = MaterialTheme.typography.headlineSmall)

                OutlinedTextField(
                    value = brandName,
                    onValueChange = { brandName = it },
                    label = { Text("Название / Имя") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = markupPercent,
                        onValueChange = { markupPercent = it },
                        label = { Text("Наценка (%)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        supportingText = { Text("Например: 50 = +50%") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = hourlyRate,
                        onValueChange = { hourlyRate = it },
                        label = { Text("Стоимость часа работы") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = defaultCurrency,
                        onValueChange = { defaultCurrency = it },
                        label = { Text("Валюта") },
                        singleLine = true,
                        modifier = Modifier.width(120.dp)
                    )
                }
            }

            HorizontalDivider()

            PriceListTable(vm = priceVm, modifier = Modifier.weight(1f).fillMaxWidth())
        }
    }
}