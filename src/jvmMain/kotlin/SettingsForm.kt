import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SettingsForm(preferencesState: MutableState<AppPreferences>) {
    var preferences by preferencesState;
    Box(Modifier.width(300.dp).padding(vertical = 15.dp, horizontal = 20.dp)) {
        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
            Row {
                Select(
                    label = "Timer Start Trigger",
                    state = preferences.timerStartTrigger,
                    setState = { preferences = preferences.copy(timerStartTrigger = it) },
                    values = listOf(
                        Pair("Automatic", TimerStartTrigger.AUTOMATIC),
                        Pair("First Input", TimerStartTrigger.FIRST_INPUT)
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