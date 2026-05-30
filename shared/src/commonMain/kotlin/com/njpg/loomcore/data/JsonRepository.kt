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
    private val file: Path, private val serializer: KSerializer<T>,
    private val getId: (T) -> Int,
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

    fun nextId(current: List<T>): Int = (current.maxOfOrNull { getId(it) } ?: 0) + 1
}