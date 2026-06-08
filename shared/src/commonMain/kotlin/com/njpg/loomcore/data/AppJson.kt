package com.njpg.loomcore.data

import kotlinx.serialization.json.Json

val AppJson = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}