package com.njpg.loomcore.data

import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.copyTo
import kotlin.io.path.name

object ImageStorage {

    /**
     * Копирует файл [source] в data/img/<имя файла>.
     * Возвращает имя файла в папке img (не полный путь).
     */
    fun importPhoto(source: Path): String {
        val dest = Paths.imgDir.resolve(source.name)
        source.copyTo(dest, StandardCopyOption.REPLACE_EXISTING)
        return source.name
    }

    /** Возвращает полный Path к фото по имени файла, или null если не существует. */
    fun resolve(fileName: String?): Path? {
        if (fileName.isNullOrBlank()) return null
        val p = Paths.imgDir.resolve(fileName)
        return if (p.toFile().exists()) p else null
    }
}
