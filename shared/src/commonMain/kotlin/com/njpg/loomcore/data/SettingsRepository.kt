package com.njpg.loomcore.data

import com.njpg.loomcore.data.SettingsRepository.cache
import com.njpg.loomcore.data.SettingsRepository.flush
import java.util.*
import kotlin.io.path.exists
import kotlin.io.path.reader
import kotlin.io.path.writer

/**
 * Хранилище мелких настроек приложения в формате .properties.
 *
 * Файл читается один раз и кешируется в [cache]. Все изменения
 * немедленно сбрасываются на диск через [flush].
 */
object SettingsRepository {

    /** Кеш загруженных настроек. Null = ещё не загружен. */
    private var cache: Properties? = null

    /**
     * Возвращает закешированный объект [Properties],
     * загружая файл при первом вызове.
     */
    private fun properties(): Properties = cache ?: Properties().also { props ->
        val file = Paths.settingsFile
        if (file.exists()) file.reader(Charsets.UTF_8).use { props.load(it) }
        cache = props
    }

    /**
     * Читает строковое значение настройки.
     *
     * @param key      Ключ настройки.
     * @param default  Значение по умолчанию, если ключ отсутствует.
     */
    fun get(key: String, default: String = ""): String = properties().getProperty(key, default)

    /**
     * Записывает значение настройки и немедленно сохраняет файл на диск.
     *
     * @param key    Ключ настройки.
     * @param value  Новое значение.
     */
    fun set(key: String, value: String) {
        properties().setProperty(key, value)
        flush()
    }

    /** Принудительно сохраняет все текущие настройки в файл. */
    fun flush() {
        Paths.settingsFile.writer(Charsets.UTF_8).use { properties().store(it, "LoomCore Settings") }
    }
}