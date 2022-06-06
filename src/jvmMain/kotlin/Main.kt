// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.kwhat.jnativehook.GlobalScreen
import java.awt.Dimension

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App(
    exit: () -> Unit,
    sizeState: MutableState<Size>
) {
    val preferencesState = remember { mutableStateOf<AppPreferences>(AppPreferences()) }
    var preferences by preferencesState
    val showSettingsFormState = remember { mutableStateOf(false) }
    val showSettingsForm by showSettingsFormState;
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val systemDarkTheme = isSystemInDarkTheme()
    val darkmode = darkthemeToBinary(preferences.colorTheme, systemDarkTheme)
    var size by sizeState
    val windowSize by remember(preferences) {
        derivedStateOf { preferences.windowSize }
    }
    val widthState = remember(sizeState) {
        derivedStateOf { sizeState.value.width }
    }
    AppPreferencesLoader(exit, preferencesState)
    LaunchedEffect(Unit) { GlobalScreen.registerNativeHook() }
    LaunchedEffect(showSettingsForm, windowSize) {
        if (showSettingsForm) {
            size = Size(600F, 600F)
        } else {
            size = when (windowSize) {
                WindowSize.XS -> Size(400F, 200F)
                WindowSize.SM -> Size(600F, 300F)
                WindowSize.MD -> Size(800F, 400F)
                WindowSize.LG -> Size(1000F, 500F)
                WindowSize.XL -> Size(1200F, 600F)
            }
        }
    }
    CustomTheme(darkmode = darkmode) {
        Box(
            Modifier.fillMaxSize().hoverable(interactionSource).background(MaterialTheme.colors.background)
        ) {
            Surface {
                if (showSettingsForm) {
                    SettingsForm(preferencesState)
                }
                TheTimer(preferencesState, 30, widthState, !showSettingsForm)
                val boState = if (showSettingsForm) {
                    ButtonOverlayState.SETTINGS
                } else {
                    ButtonOverlayState.TIMER
                }
                if (hovered || showSettingsForm) {
                    ButtonOverlay(
                        exit,
                        showSettingsFormState,
                        state = boState,
                        windowSize = preferences.windowSize,
                        setWindowSize = { preferences = preferences.copy(windowSize = it) }
                    )
                }
            }
        }
    }
}

@Composable
fun CustomTheme(darkmode: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(colors = if (darkmode) darkColors() else lightColors(), content = content)
}


fun main() = application {
    val sizeState = remember { mutableStateOf<Size>(Size(0F, 0F)) }
    val size by sizeState
    Window(
        onCloseRequest = ::exitApplication,
        undecorated = true,
        alwaysOnTop = true,
        resizable = false
    ) {
        LaunchedEffect(size) {
            window.size = Dimension(size.width.toInt(), size.height.toInt())
        }
        WindowDraggableArea {
            App(::exitApplication, sizeState)
        }
    }
}
