package com.njpg.loomcore.data

import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.toPath

object Paths {

    /** Директория, рядом с которой лежит исполняемый файл. */
    val appDir: Path by lazy {
        val uri = Paths::class.java.protectionDomain?.codeSource?.location?.toURI()
        if (uri != null) {
            val jarPath = uri.toPath()
            resolveAppDir(jarPath)
        } else {
            Path.of(System.getProperty("user.dir"))
        }
    }

    val dataDir: Path by lazy { appDir.resolve("data").also { it.createDirectories() } }
    val settingsDir: Path by lazy { dataDir.resolve("settings").also { it.createDirectories() } }
    val dbDir: Path by lazy { dataDir.resolve("db").also { it.createDirectories() } }
    val imgDir: Path by lazy { dataDir.resolve("img").also { it.createDirectories() } }

    val settingsFile: Path by lazy { settingsDir.resolve("settings.properties") }
    val unitsFile: Path by lazy { dbDir.resolve("units.json") }
    val suppliersFile: Path by lazy { dbDir.resolve("suppliers.json") }
    val clientsFile: Path by lazy { dbDir.resolve("clients.json") }

    private fun resolveAppDir(jarPath: Path): Path {
        var dir = if (jarPath.toFile().isFile) jarPath.parent else jarPath
        if (dir.fileName?.toString() == "app") dir = dir.parent
        return dir
    }
}