import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.SwingKeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.VK_UNDEFINED

@kotlinx.serialization.Serializable
data class KeyCombination(val keyCode: Int, val modifiers: Int) {
    override fun toString(): String {
        if (modifiers == 0 && keyCode == VK_UNDEFINED) {
            return "None"
        }
        return "${
            if (modifiers != 0) {
                "${KeyEvent.getModifiersExText(modifiers)}+"
            } else {
                ""
            }
        }${KeyEvent.getKeyText(keyCode)}"
    }

    companion object {

        fun fromAWTKeyEvent(keyEvent: KeyEvent): KeyCombination {
            return KeyCombination(keyEvent.keyCode, keyEvent.modifiersEx)
        }
    }
}

class KeyBinder(val onKeyPress: (KeyCombination) -> Unit) : SwingKeyAdapter() {
    override fun keyPressed(keyEvent: KeyEvent?) {
        super.keyPressed(keyEvent)
        if (keyEvent != null) {
            onKeyPress(KeyCombination.fromAWTKeyEvent(keyEvent))
        }
    }
}