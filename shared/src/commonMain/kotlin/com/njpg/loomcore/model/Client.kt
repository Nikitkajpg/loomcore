package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

/**
 * Покупатель / заказчик.
 *
 * @property id          Уникальный числовой идентификатор (генерируется репозиторием).
 * @property name        Имя или название организации.
 * @property phoneNumber Контактный телефон в произвольном формате.
 * @property url         Ссылка на соцсеть, сайт или мессенджер.
 * @property notes       Произвольные заметки о клиенте.
 */
@Serializable
data class Client(
    override val id: Int,
    val name: String,
    val phoneNumber: String = "",
    val url: String = "",
    val notes: String = ""
) : HasId