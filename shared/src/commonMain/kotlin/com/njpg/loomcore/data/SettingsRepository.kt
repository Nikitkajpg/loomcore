package com.njpg.loomcore.data

import java.util.*
import kotlin.io.path.exists
import kotlin.io.path.reader
import kotlin.io.path.writer

object SettingsRepository {

    private val file = Paths.settingsFile

    fun load(): Properties {
        val props = Properties()
        if (file.exists()) file.reader(Charsets.UTF_8).use { props.load(it) }
        return props
    }

    fun save(props: Properties) {
        file.writer(Charsets.UTF_8).use { props.store(it, "LoomCore Settings") }
    }

    fun get(key: String, default: String = ""): String = load().getProperty(key, default)

    fun set(key: String, value: String) {
        val props = load()
        props.setProperty(key, value)
        save(props)
    }
}