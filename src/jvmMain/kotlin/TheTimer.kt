import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.github.kwhat.jnativehook.GlobalScreen
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.Instant


@Composable
fun TheTimer(
    preferencesState: State<AppPreferences>,
    updatesPerSecond: Int,
    width: State<Float>,
    visible: Boolean
) {
    var lastTimestamp by rememberSaveable { mutableStateOf<Instant?>(null) }
    var add by rememberSaveable { mutableStateOf<Duration>(Duration.ZERO) }
    var formatted by remember { mutableStateOf<String>("00:00.00") }
    val preferences by preferencesState;
    val density = LocalDensity.current;
    val textSize by remember(width) { derivedStateOf { with(density) { (width.value / 7).toSp() } } }
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
            val now = Instant.now()
            val realtime = Duration.between(lastTimestamp, now) + add
            formatted = format(realtime)
            delay((1000 / updatesPerSecond).toLong())
        }
    }
    LaunchedEffect(add) {
        formatted = if (add == Duration.ZERO)
            "00:00.00"
        else
            format(add)
    }
    if (visible) {
        Box(modifier = Modifier.fillMaxSize().padding(Dp.Hairline)) {
            Column(verticalArrangement = Arrangement.spacedBy(17.dp)) {
                Text(
                    text = formatted,
                    modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp, vertical = 0.dp),
                    fontSize = textSize,
                    textAlign = TextAlign.Start,
                    lineHeight = textSize / 1000,
                    maxLines = 1,
                    color = Color.Gray
                )
            }
        }
    }
}