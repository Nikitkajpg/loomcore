package com.njpg.loomcore.data

import kotlinx.serialization.json.Json

/**
 * Единственный экземпляр [Json]-сериализатора для всего приложения.
 */
val AppJson = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}