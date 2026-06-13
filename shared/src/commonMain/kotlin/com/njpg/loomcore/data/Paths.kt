package com.njpg.loomcore.data

import com.njpg.loomcore.data.Paths.appDir
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.toPath

/**
 * Централизованное хранилище всех путей файловой системы приложения.
 *
 * Все пути вычисляются лениво (lazy) при первом обращении.
 * Структура директорий создаётся автоматически через [createDirectories].
 *
 * [appDir] определяется по местоположению JAR-файла.
 */
object Paths {
    /**
     * Папка, в которой находится JAR-файл (или рабочая папка IDE).
     * Используется как точка отсчёта для всех остальных путей.
     */
    val appDir: Path by lazy {
        val uri = Paths::class.java.protectionDomain?.codeSource?.location?.toURI()
        if (uri != null) resolveAppDir(uri.toPath())
        else Path.of(System.getProperty("user.dir"))
    }

    val appParentDir: Path by lazy { appDir.parent ?: appDir }

    /** Корневая папка данных приложения рядом с исполняемым файлом. */
    val dataDir: Path by lazy { appParentDir.resolve("LoomCoreData").also { it.createDirectories() } }

    /** Папка для настроек (properties-файлы). */
    val settingsDir: Path by lazy { dataDir.resolve("settings").also { it.createDirectories() } }

    /** Папка для JSON-файлов базы данных. */
    val dbDir: Path by lazy { dataDir.resolve("db").also { it.createDirectories() } }

    /** Папка для изображений изделий. */
    val imgDir: Path by lazy { dataDir.resolve("img").also { it.createDirectories() } }

    val settingsFile: Path by lazy { settingsDir.resolve("settings.properties") }
    val profileFile: Path by lazy { dbDir.resolve("profile.json") }
    val productsFile: Path by lazy { dbDir.resolve("products.json") }
    val suppliersFile: Path by lazy { dbDir.resolve("suppliers.json") }
    val clientsFile: Path by lazy { dbDir.resolve("clients.json") }
    val materialsFile: Path by lazy { dbDir.resolve("materials.json") }
    val priceListFile: Path by lazy { dbDir.resolve("price-list.json") }
    val ordersFile: Path by lazy { dbDir.resolve("orders.json") }

    /**
     * Поднимается по иерархии директорий, чтобы найти корневую папку рядом с JAR.
     *
     * Если путь указывает на файл — берёт его родительскую директорию.
     * Пропускает папку "app" (стандартная структура Compose Desktop-дистрибутива).
     */
    private fun resolveAppDir(jarPath: Path): Path {
        var dir = if (jarPath.toFile().isFile) jarPath.parent else jarPath
        if (dir.fileName?.toString() == "app") dir = dir.parent
        return dir
    }
}