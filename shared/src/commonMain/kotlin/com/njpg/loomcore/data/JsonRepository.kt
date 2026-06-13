package com.njpg.loomcore.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

/**
 * Универсальный репозиторий для хранения списка сущностей в JSON-файле.
 *
 * Каждый тип данных (клиенты, материалы, заказы и т.д.) имеет свой
 * экземпляр этого класса с указанием своего файла и сериализатора.
 * Это позволяет не дублировать логику чтения/записи.
 *
 * Данные хранятся в простом JSON-массиве на диске.
 *
 * @param T           Тип хранимой сущности.
 * @param file        Путь к JSON-файлу на диске.
 * @param serializer  KSerializer для типа [T] (обычно `T.serializer()`).
 * @param getId       Лямбда, извлекающая уникальный Int-идентификатор из [T].
 */
class JsonRepository<T>(
    private val file: Path,
    private val serializer: KSerializer<T>,
    private val getId: (T) -> Int,
) {
    /**
     * Загружает все элементы из файла.
     *
     * Если файл не существует или повреждён — возвращает пустой список,
     * что позволяет корректно запуститься при первом использовании.
     */
    fun loadAll(): List<T> {
        if (!file.exists()) return emptyList()
        return try {
            AppJson.decodeFromString(ListSerializer(serializer), file.readText())
        } catch (_: Exception) {
            emptyList()
        }
    }

    /**
     * Перезаписывает файл полным актуальным списком [items].
     */
    fun saveAll(items: List<T>) {
        file.writeText(AppJson.encodeToString(ListSerializer(serializer), items))
    }

    /**
     * Вычисляет следующий свободный идентификатор.
     *
     * Берёт максимальный id из [current] и прибавляет 1.
     * При пустом списке возвращает 1.
     */
    fun nextId(current: List<T>): Int = (current.maxOfOrNull { getId(it) } ?: 0) + 1
}