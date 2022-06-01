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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App(
    exit: () -> Unit,
    resizableState: MutableState<Boolean>,
    widthState: MutableState<Int>,
    heightState: MutableState<Int>
) {
    val preferencesState = remember { mutableStateOf<AppPreferences>(AppPreferences()) }
    var preferences by preferencesState
    val showSettingsFormState = remember { mutableStateOf(false) }
    val showSettingsForm by showSettingsFormState;
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val systemDarkTheme = isSystemInDarkTheme()
    val darkmode = darkthemeToBinary(preferences.colorTheme, systemDarkTheme)
    var resizable by resizableState
    var width by widthState
    var height by heightState
    AppPreferencesLoader(exit, preferencesState)
    LaunchedEffect(showSettingsForm) {
        resizable = !showSettingsForm;
        if (showSettingsForm) {
            width = 500
            height = 500
        } else {
            width = preferences.widthInt
            height = preferences.heightInt
        }
    }
    CustomTheme(darkmode = darkmode) {
        Box(
            Modifier.fillMaxSize().hoverable(interactionSource).background(MaterialTheme.colors.background)
                .onSizeChanged {
                    if (!showSettingsForm) {
                        preferences = preferences.copy(widthInt = it.width, heightInt = it.height)
                    }
                }
        ) {
            Surface {
                if (showSettingsForm) {
                    SettingsForm(preferencesState)
                }
                if (hovered || showSettingsForm) {
                    ButtonOverlay(exit, showSettingsFormState)
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
    val resizableState = remember { mutableStateOf(false) }
    val resizable by resizableState;
    val widthState = remember { mutableStateOf(0) }
    val width by widthState
    val heightState = remember { mutableStateOf(0) }
    val height by heightState
    Window(
        onCloseRequest = ::exitApplication,
        undecorated = true,
        alwaysOnTop = true,
        resizable = resizable
    ) {
        LaunchedEffect(width, height) {
            window.setSize(width, height)
        }
        WindowDraggableArea {
            App(::exitApplication, resizableState, widthState, heightState)
        }
    }
}
