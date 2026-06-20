package com.njpg.loomcore

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.njpg.loomcore.ui.screen.MainScreen
import com.njpg.loomcore.ui.screen.Screen
import com.njpg.loomcore.ui.screen.SplashScreen
import com.njpg.loomcore.update.UpdateAvailableDialog
import com.njpg.loomcore.viewmodel.CoreViewModel

@Composable
fun App(vm: CoreViewModel = viewModel { CoreViewModel() }) {
    val updateState by vm.updateState.collectAsStateWithLifecycle()

    MaterialTheme(colorScheme = darkColorScheme()) {
        var screen by remember { mutableStateOf(Screen.Splash) }
        when (screen) {
            Screen.Splash -> SplashScreen(onFinished = { screen = Screen.Main })
            Screen.Main -> MainScreen()
        }

        if (!updateState.dismissed) {
            updateState.availableUpdate?.let { update ->
                UpdateAvailableDialog(
                    update = update, onDismiss = { vm.dismissUpdate() })
            }
        }
    }
}

