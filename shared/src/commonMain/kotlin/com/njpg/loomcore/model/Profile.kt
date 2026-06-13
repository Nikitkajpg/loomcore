package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

/**
 * Профиль производителя — глобальные настройки расчётов.
 *
 * Единственный объект в приложении.
 * Влияет на автоматический расчёт цен во всех новых заказах.
 *
 * ## Формула расчёта цены заказа PRODUCT
 * ```
 * себестоимость = Σ(материал.costPerUnit × расход) + workTimeHours × hourlyRate
 * итоговая цена = себестоимость × (1 + markupPercent / 100)
 * ```
 *
 * @property id              Всегда 1 (один профиль на приложение).
 * @property brandName       Название бренда или имя мастера.
 * @property markupPercent   Наценка в процентах.
 * @property hourlyRate      Стоимость одного часа работы мастера.
 * @property defaultCurrency Обозначение валюты для отображения цен.
 */
@Serializable
data class Profile(
    val id: Int = 1,
    val brandName: String = "",
    val markupPercent: Double = 0.0,
    val hourlyRate: Double = 0.0,
    val defaultCurrency: String = "Br",
)