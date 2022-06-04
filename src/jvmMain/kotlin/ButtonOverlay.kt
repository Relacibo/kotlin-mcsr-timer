import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class ButtonOverlayState {
    TIMER,
    SETTINGS,
}

@Composable
fun ButtonOverlay(
    exit: () -> Unit,
    showSettingsFormState: MutableState<Boolean>,
    state: ButtonOverlayState
) {
    var showSettingsForm by showSettingsFormState;
    val systemDarkTheme = isSystemInDarkTheme()
    Box(Modifier.fillMaxSize().padding(PaddingValues(horizontal = 9.dp, vertical = 6.dp))) {
        Row(Modifier.align(Alignment.TopEnd)) {
            if (state == ButtonOverlayState.TIMER) {
                IconButton(onClick = { showSettingsForm = true }) {
                    Icon(
                        Icons.Outlined.Settings,
                        contentDescription = "Settings"
                    )
                }
            } else {
                IconButton(onClick = { showSettingsForm = false }) {
                    Icon(
                        Icons.Outlined.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
            IconButton(
                onClick = exit
            ) { Icon(Icons.Outlined.Close, contentDescription = "Close") }
        }
    }

}