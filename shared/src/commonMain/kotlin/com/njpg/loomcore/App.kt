package com.njpg.loomcore

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import com.njpg.loomcore.ui.screen.MainScreen
import com.njpg.loomcore.ui.screen.Screen
import com.njpg.loomcore.ui.screen.SplashScreen

@Composable
fun App() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        var screen by remember { mutableStateOf(Screen.Splash) }
        when (screen) {
            Screen.Splash -> SplashScreen(onFinished = { screen = Screen.Main })
            Screen.Main -> MainScreen()
        }
    }
}

