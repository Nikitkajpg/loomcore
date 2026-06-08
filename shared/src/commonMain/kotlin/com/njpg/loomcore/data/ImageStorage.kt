package com.njpg.loomcore.data

import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.copyTo
import kotlin.io.path.deleteIfExists
import kotlin.io.path.name

object ImageStorage {

    fun importPhoto(source: Path): String {
        val dest = Paths.imgDir.resolve(source.name)
        source.copyTo(dest, StandardCopyOption.REPLACE_EXISTING)
        return source.name
    }

    fun resolve(fileName: String?): Path? {
        if (fileName.isNullOrBlank()) return null
        val p = Paths.imgDir.resolve(fileName)
        return if (p.toFile().exists()) p else null
    }

    fun deletePhoto(fileName: String?) {
        resolve(fileName)?.deleteIfExists()
    }
}
