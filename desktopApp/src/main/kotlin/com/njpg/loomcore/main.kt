package com.njpg.loomcore

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.njpg.loomcore.data.SettingsRepository

fun main() = application {
    val savedMaximized = SettingsRepository.get("window.maximized", "true").toBoolean()
    val windowState = WindowState(
        placement = if (savedMaximized) WindowPlacement.Maximized else WindowPlacement.Floating
    )

    Window(
        onCloseRequest = {
            SettingsRepository.set(
                "window.maximized", (windowState.placement == WindowPlacement.Maximized).toString()
            )
            exitApplication()
        },
        title = "LoomCore",
        state = windowState,
    ) {
        App()
    }
}