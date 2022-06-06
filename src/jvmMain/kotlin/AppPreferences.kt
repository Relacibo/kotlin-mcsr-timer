import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.writeLines

private object Constants {
    val FORMAT = Json { prettyPrint = true; isLenient = true }
    val FILE_PATH = Path("./config.json")
}

@Serializable
data class AppPreferences constructor(
    var colorTheme: ColorTheme = ColorTheme.SYSTEM,
    var timerStartTrigger: TimerStartTrigger = TimerStartTrigger.WORLD_START,
    var timerStartShortcut: KeyCombination? = null,
    var timerEndTrigger: TimerEndTrigger = TimerEndTrigger.END_SCREEN,
    var timerEndShortcut: KeyCombination? = null,
    var timerResetShortcut: KeyCombination? = null,
    var windowSize: WindowSize = WindowSize.MD,
    var updatesPerSecond: UpdatesPerSecond = UpdatesPerSecond.N30
)

@Serializable
enum class TimerStartTrigger {
    WORLD_START,
    FIRST_INPUT,
    TIME_SET_ZERO,
    NONE,
}

@Serializable
enum class TimerEndTrigger {
    END_SCREEN
}

@Serializable
enum class ColorTheme {
    SYSTEM,
    DARK,
    LIGHT,
}

@Serializable
enum class WindowSize {
    XS,
    SM,
    MD,
    LG,
    XL
}

@Serializable
enum class UpdatesPerSecond {
    N15,
    N30,
    N60,
    N144,
}

fun incrementWindowSize(ws: WindowSize): WindowSize {
    return when (ws) {
        WindowSize.XS -> WindowSize.SM
        WindowSize.SM -> WindowSize.MD
        WindowSize.MD -> WindowSize.LG
        WindowSize.LG -> WindowSize.XL
        else -> ws
    }
}

fun decrementWindowSize(ws: WindowSize): WindowSize {
    return when (ws) {
        WindowSize.SM -> WindowSize.XS
        WindowSize.MD -> WindowSize.SM
        WindowSize.LG -> WindowSize.MD
        WindowSize.XL -> WindowSize.LG
        else -> ws
    }
}

fun darkthemeToBinary(theme: ColorTheme, systemDarkTheme: Boolean): Boolean {
    return when (theme) {
        ColorTheme.SYSTEM -> systemDarkTheme
        ColorTheme.DARK -> true
        ColorTheme.LIGHT -> false
    }
}

private fun writeToFile(preferences: AppPreferences) {
    val serialized = Constants.FORMAT.encodeToString(preferences);
    Constants.FILE_PATH.writeLines(serialized.split("\n"))
}

@Composable
fun AppPreferencesLoader(exit: () -> Unit, preferencesState: MutableState<AppPreferences>) {
    var preferences by preferencesState;
    var error by remember { mutableStateOf<Exception?>(null) }

    LaunchedEffect(Unit) {
        try {
            val lines = Constants.FILE_PATH.readLines().joinToString(" ");
            preferences = Constants.FORMAT.decodeFromString<AppPreferences>(lines)
        } catch (e: java.nio.file.NoSuchFileException) {
            preferences = AppPreferences()
        } catch (e: SerializationException) {
            error = e
        }
    }
    LaunchedEffect(preferences) {
        writeToFile(preferences)
    }
    error?.let {
        Popup(alignment = Alignment.Center) {
            Box(
                Modifier.background(Color.LightGray, RoundedCornerShape(6.dp))
            ) {
                Column(Modifier.padding(10.dp)) {
                    it.localizedMessage?.let { Text(it) };
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = Green),
                        onClick = {
                            preferences = AppPreferences()
                            error = null
                        }
                    ) { Text("Restore default preferences") }
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = Red),
                        onClick = exit
                    ) { Text("Close Application") }
                }
            }
        }
    }
}