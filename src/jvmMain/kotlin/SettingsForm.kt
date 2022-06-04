import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp


@Composable
fun SettingsForm(preferencesState: MutableState<AppPreferences>) {
    var preferences by preferencesState
    var textfieldSize by remember { mutableStateOf(IntSize.Zero) }
    var startKeybind by remember { mutableStateOf<String?>(null) }
    Box(Modifier.width(300.dp).padding(vertical = 15.dp, horizontal = 20.dp)) {
        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(15.dp)) {
            Row {
                Select(
                    label = "Timer Start Trigger",
                    state = preferences.timerStartTrigger,
                    setState = { preferences = preferences.copy(timerStartTrigger = it) },
                    values = listOf(
                        Pair("World Start", TimerStartTrigger.WORLD_START),
                        Pair("First Input", TimerStartTrigger.FIRST_INPUT),
                        Pair(
                            "None", TimerStartTrigger.NONE
                        )
                    ),
                )
            }
            Box {
                OutlinedTextField(
                    value = if (startKeybind != null) startKeybind!! else "Unassigned",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth().onGloballyPositioned { coordinates ->
                        textfieldSize = coordinates.size
                    },
                    label = { Text("Start Keybind") }
                )
                Box(
                    Modifier.size(width = textfieldSize.width.dp, height = textfieldSize.height.dp))
            }
            Row {
                Select(
                    label = "Timer End Trigger",
                    state = preferences.timerEndTrigger,
                    setState = { preferences = preferences.copy(timerEndTrigger = it) },
                    values = listOf(
                        Pair("EndScreen", TimerEndTrigger.END_SCREEN),
                    ),
                )
            }
            Row {
                Select(
                    label = "Theme",
                    state = preferences.colorTheme,
                    setState = { preferences = preferences.copy(colorTheme = it) },
                    values = listOf(
                        Pair("System", ColorTheme.SYSTEM),
                        Pair("Light", ColorTheme.LIGHT),
                        Pair("Dark", ColorTheme.DARK)
                    ),
                )
            }
        }
    }
}