import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ButtonOverlay(
    exit: () -> Unit,
    showSettingsFormState: MutableState<Boolean>
) {
    var showSettingsForm by showSettingsFormState;
    val systemDarkTheme = isSystemInDarkTheme()
    Box(Modifier.fillMaxSize().padding(PaddingValues(horizontal = 9.dp, vertical = 6.dp))) {
        Row(Modifier.align(Alignment.TopEnd)) {
            IconButton(onClick = { showSettingsForm = !showSettingsForm }) {
                Icon(
                    Icons.Outlined.Settings,
                    contentDescription = "Settings"
                )
            }
            IconButton(
                onClick = exit
            ) { Icon(Icons.Outlined.Close, contentDescription = "Close") }
        }
    }

}