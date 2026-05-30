package com.njpg.loomcore.data

import kotlinx.serialization.KSerializer
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

class SingleObjectRepository<T>(
    private val file: Path,
    private val serializer: KSerializer<T>,
    private val default: () -> T,
) {
    fun load(): T {
        if (!file.exists()) return default()
        return try {
            AppJson.decodeFromString(serializer, file.readText())
        } catch (_: Exception) {
            default()
        }
    }

    fun save(value: T) {
        file.writeText(AppJson.encodeToString(serializer, value))
    }
}