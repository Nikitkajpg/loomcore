package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

/**
 * Покупатель / заказчик.
 *
 * Используется для привязки заказов к конкретному человеку.
 * Все поля, кроме [id] и [name], необязательны.
 *
 * @property id          Уникальный числовой идентификатор (генерируется репозиторием).
 * @property name        Имя или название организации.
 * @property phoneNumber Контактный телефон в произвольном формате.
 * @property url         Ссылка на соцсеть, сайт или мессенджер.
 * @property notes       Произвольные заметки о клиенте.
 */
@Serializable
data class Client(
    val id: Int,
    val name: String,
    val phoneNumber: String = "",
    val url: String = "",
    val notes: String = ""
)