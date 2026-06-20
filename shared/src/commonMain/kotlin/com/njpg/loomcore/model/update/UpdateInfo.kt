package com.njpg.loomcore.model.update

import com.njpg.loomcore.core.AppInfo

data class UpdateInfo(
    val currentVersion: String = AppInfo.VERSION,
    val latestVersion: String,
    val releaseUrl: String,
    val releaseNotes: String? = null
) {
    val isUpdateAvailable: Boolean
        get() {
            fun parse(v: String) = v.trim().split(".").map { it.toIntOrNull() ?: 0 }

            val a = parse(currentVersion)
            val b = parse(latestVersion)
            val max = maxOf(a.size, b.size)

            for (i in 0 until max) {
                val x = a.getOrElse(i) { 0 }
                val y = b.getOrElse(i) { 0 }
                if (y > x) return true
                if (y < x) return false
            }
            return false
        }
}