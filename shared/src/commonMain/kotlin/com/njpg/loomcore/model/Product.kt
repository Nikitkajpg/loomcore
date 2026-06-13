package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

/**
 * Шаблон изделия — предустановка для быстрого создания заказа на пошив.
 *
 * @property id            Уникальный идентификатор.
 * @property name          Название изделия.
 * @property materialsUsed Список материалов и их расхода по умолчанию.
 * @property workTimeHours Стандартное время пошива в часах.
 * @property photoPaths    Имена файлов фотографий.
 * @property notes         Произвольные заметки о конструкции или фурнитуре.
 */
@Serializable
data class Product(
    val id: Int,
    val name: String,
    val materialsUsed: List<MaterialUsage> = emptyList(),
    val workTimeHours: Double = 0.0,
    val photoPaths: List<String> = emptyList(),
    val notes: String = ""
)