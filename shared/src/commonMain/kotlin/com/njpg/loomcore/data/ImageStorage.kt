package com.njpg.loomcore.data

import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.copyTo
import kotlin.io.path.deleteIfExists
import kotlin.io.path.name

/**
 * Менеджер хранилища фотографий изделий.
 *
 * Все изображения хранятся в директории [Paths.imgDir].
 */
object ImageStorage {

    /**
     * Копирует фото из произвольного пути [source] в папку приложения.
     *
     * Если файл с таким именем уже существует — перезаписывает его.
     *
     * @param source  Исходный путь к файлу.
     * @return        Имя файла (без директории), которое нужно сохранить в модель.
     */
    fun importPhoto(source: Path): String {
        val dest = Paths.imgDir.resolve(source.name)
        source.copyTo(dest, StandardCopyOption.REPLACE_EXISTING)
        return source.name
    }

    /**
     * Возвращает абсолютный путь к файлу по его имени, если файл существует.
     *
     * @param fileName  Имя файла из модели.
     * @return          Путь к файлу или null, если файл не найден.
     */
    fun resolve(fileName: String?): Path? {
        if (fileName.isNullOrBlank()) return null
        val p = Paths.imgDir.resolve(fileName)
        return if (p.toFile().exists()) p else null
    }

    /**
     * Удаляет фото из папки приложения.
     * Если файл не существует — операция игнорируется.
     *
     * @param fileName  Имя файла, который нужно удалить.
     */
    fun deletePhoto(fileName: String?) {
        resolve(fileName)?.deleteIfExists()
    }
}
