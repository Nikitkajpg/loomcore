package com.njpg.loomcore

import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.njpg.loomcore.data.SettingsRepository
import javax.imageio.ImageIO

fun main() = application {
    val savedMaximized = SettingsRepository.get("window.maximized", "true").toBoolean()
    val windowState = WindowState(
        placement = if (savedMaximized) WindowPlacement.Maximized else WindowPlacement.Floating
    )
    val iconStream = Thread.currentThread().contextClassLoader.getResourceAsStream("logo.png")
    val appIcon = iconStream?.use { stream ->
        ImageIO.read(stream).toComposeImageBitmap()
    }

    Window(
        onCloseRequest = {
            SettingsRepository.set(
                "window.maximized", (windowState.placement == WindowPlacement.Maximized).toString()
            )
            exitApplication()
        },
        title = "LoomCore",
        icon = appIcon?.let { BitmapPainter(it) },
        state = windowState,
    ) {
        App()
    }
}