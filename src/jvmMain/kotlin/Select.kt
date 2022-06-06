import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

@Composable
fun <T> Select(
    label: String,
    state: T,
    setState: (T) -> Unit,
    values: List<Pair<String, T>>
) {
    fun stateToValue(arg: T): String? {
        return values.find { (s, v) -> v == arg }?.first
    }

    var textfieldSize by remember { mutableStateOf(IntSize.Zero) }
    var expanded by remember { mutableStateOf(false) }
    Column {
        Box {
            OutlinedTextField(
                value = stateToValue(state)!!,
                onValueChange = { },
                modifier = Modifier.fillMaxWidth().onGloballyPositioned { coordinates ->
                    textfieldSize = coordinates.size
                },
                label = { Text(label) },
            )
            Box(
                Modifier.size(width = textfieldSize.width.dp, height = textfieldSize.height.dp)
                    .clickable { expanded = true })
        }
        DropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }) {
            values.forEach { (label, value) ->
                DropdownMenuItem(onClick = {
                    setState(value)
                    expanded = false
                }) {
                    Text(text = label)
                }
            }
        }
    }
}