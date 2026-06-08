package com.njpg.loomcore.data

import java.util.*
import kotlin.io.path.exists
import kotlin.io.path.reader
import kotlin.io.path.writer

object SettingsRepository {

    private var cache: Properties? = null

    private fun properties(): Properties = cache ?: Properties().also { props ->
        val file = Paths.settingsFile
        if (file.exists()) file.reader(Charsets.UTF_8).use { props.load(it) }
        cache = props
    }

    fun get(key: String, default: String = ""): String = properties().getProperty(key, default)

    fun set(key: String, value: String) {
        properties().setProperty(key, value)
        flush()
    }

    fun flush() {
        Paths.settingsFile.writer(Charsets.UTF_8).use { properties().store(it, "LoomCore Settings") }
    }
}