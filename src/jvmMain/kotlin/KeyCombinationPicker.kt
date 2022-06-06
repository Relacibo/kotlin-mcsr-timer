import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEvent
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.SwingKeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun KeyCombinationPicker(label: String, state: KeyCombination?, setState: (KeyCombination?) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        OutlinedTextField(
            value = state?.toString() ?: "",
            onValueChange = {  },
            modifier = Modifier.width(260.dp).onKeyEvent {
                val event = it.awtEventOrNull;
                event?.let { ev ->
                    val isModifier =
                        ev.keyCode == VK_ALT || ev.keyCode == VK_ALT_GRAPH || ev.keyCode == VK_CONTROL || ev.keyCode == VK_META || ev.keyCode == VK_SHIFT;
                    if (ev.keyCode == VK_UNDEFINED || ev.keyCode == 0 || isModifier) {
                        return@onKeyEvent true
                    }
                    setState(KeyCombination.fromAWTKeyEvent(ev))
                }

                true
            },
            label = {
                Text(label)
            })
        Button(onClick = { setState(null) }, content = { Text("Reset") })
    }
}