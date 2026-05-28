package com.njpg.loomcore.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

class JsonRepository<T>(
    private val file: Path, private val serializer: KSerializer<T>
) {
    fun loadAll(): List<T> {
        if (!file.exists()) return emptyList()
        return try {
            json.decodeFromString(ListSerializer(serializer), file.readText())
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun saveAll(items: List<T>) {
        file.writeText(json.encodeToString(ListSerializer(serializer), items))
    }
}