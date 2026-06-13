package com.njpg.loomcore.data

import kotlinx.serialization.KSerializer
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

/**
 * Репозиторий для хранения единственного объекта в JSON-файле.
 *
 * Используется там, где нужно сохранить один объект целиком, а не список —
 * например, [Profile] пользователя.
 *
 * @param T           Тип хранимого объекта.
 * @param file        Путь к JSON-файлу на диске.
 * @param serializer  KSerializer для типа [T].
 * @param default     Фабрика объекта по умолчанию — возвращается при первом запуске.
 */
class SingleObjectRepository<T>(
    private val file: Path,
    private val serializer: KSerializer<T>,
    private val default: () -> T,
) {
    /**
     * Загружает объект из файла.
     *
     * Если файл не существует или повреждён — возвращает [default()],
     * не бросая исключение. Это безопасно при первом запуске.
     */
    fun load(): T {
        if (!file.exists()) return default()
        return try {
            AppJson.decodeFromString(serializer, file.readText())
        } catch (_: Exception) {
            default()
        }
    }

    /**
     * Перезаписывает файл актуальным значением [value].
     */
    fun save(value: T) {
        file.writeText(AppJson.encodeToString(serializer, value))
    }
}