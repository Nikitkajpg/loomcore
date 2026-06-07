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

    private val appParentDir: Path by lazy { appDir.parent ?: appDir }
    val dataDir: Path by lazy { appParentDir.resolve("LoomCoreData").also { it.createDirectories() } }
    val settingsDir: Path by lazy { dataDir.resolve("settings").also { it.createDirectories() } }
    val dbDir: Path by lazy { dataDir.resolve("db").also { it.createDirectories() } }
    val imgDir: Path by lazy { dataDir.resolve("img").also { it.createDirectories() } }

    val settingsFile: Path by lazy { settingsDir.resolve("settings.properties") }
    val profileFile: Path by lazy { dbDir.resolve("profile.json") }
    val productsFile: Path by lazy { dbDir.resolve("products.json") }
    val suppliersFile: Path by lazy { dbDir.resolve("suppliers.json") }
    val clientsFile: Path by lazy { dbDir.resolve("clients.json") }
    val materialsFile: Path by lazy { dbDir.resolve("materials.json") }
    val priceListFile: Path by lazy { dbDir.resolve("price-list.json") }

    private fun resolveAppDir(jarPath: Path): Path {
        var dir = if (jarPath.toFile().isFile) jarPath.parent else jarPath
        if (dir.fileName?.toString() == "app") dir = dir.parent
        return dir
    }
}