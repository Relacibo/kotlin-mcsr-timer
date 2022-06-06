import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.NativeHookException
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.Instant


@Composable
fun TheTimer(preferencesState: State<AppPreferences>, updatesPerSecond: Int, height: Int) {
    var lastTimestamp by remember { mutableStateOf<Instant?>(null) }
    var add by remember { mutableStateOf<Duration>(Duration.ZERO) }
    var formatted by remember { mutableStateOf<String>("00:00.00") }
    var textSize by remember { mutableStateOf<TextUnit>(12.sp) }
    val density = LocalDensity.current;
    LaunchedEffect(height) {
        println()
        with(density) {
            textSize = height.toSp()
        }
    }
    DisposableEffect(Unit) {
        val keyBinder = KeyBinder {
            if (lastTimestamp != null) {
                if (it == preferencesState.value.timerEndShortcut) {
                    val dur = Duration.between(lastTimestamp, Instant.now())
                    add += dur
                    lastTimestamp = null
                }
            } else {
                if (it == preferencesState.value.timerStartShortcut) {
                    lastTimestamp = Instant.now()
                } else if (it == preferencesState.value.timerResetShortcut) {
                    add = Duration.ZERO
                }
            }
        }
        GlobalScreen.addNativeKeyListener(keyBinder);
        onDispose {
            GlobalScreen.removeNativeKeyListener(keyBinder)
        }
    }
    fun format(rt: Duration): String {
        val hours = rt.seconds / 3600
        val minutes = (rt.seconds % 3600) / 60
        val seconds = rt.seconds % 60
        val tenths = rt.toMillisPart() / 10
        val hoursString = if (hours > 0L) "${hours}:" else ""
        return hoursString + String.format("%02d:%02d.%02d", minutes, seconds, tenths)
    }
    LaunchedEffect(lastTimestamp) {
        while (lastTimestamp != null) {
            val realtime = Duration.between(lastTimestamp, Instant.now()) + add
            formatted = format(realtime)
            delay((1000 / updatesPerSecond).toLong())
        }
    }
    LaunchedEffect(add) {
        if (add == Duration.ZERO)
            formatted = "00:00.00"
        else
            formatted = format(add)
    }
    Text(
        text = formatted,
        modifier = Modifier.fillMaxSize().padding(Dp.Hairline),
        fontSize = textSize
    )
}