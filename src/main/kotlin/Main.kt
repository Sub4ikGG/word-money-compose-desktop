import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    MaterialTheme {
        val input = remember { mutableStateOf("") }
        val result: MutableState<String?> = remember { mutableStateOf(null) }

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(text = "Слова в числа", style = TextStyle(fontSize = 16.sp, color = Color.Gray))
            OutlinedTextField(
                modifier = Modifier.padding(top = 8.dp),
                value = input.value,
                onValueChange = {
                    input.value = it
                    result.value = if (it.isNotBlank()) {
                        try {
                            convertWordToInt(word = it).toString()
                        } catch (e: Exception) { e.localizedMessage }
                    } else null
                },
                textStyle = TextStyle(fontSize = 20.sp),
                placeholder = { Text(text = "Конструкции", style = TextStyle(fontSize = 20.sp)) }
            )

            OutlinedButton(
                content = { Text(text = "Выдать результат") },
                onClick = {
                    result.value = if (input.value.isNotBlank()) {
                        try {
                            convertWordToInt(word = input.value).toString()
                        } catch (e: Exception) { e.localizedMessage }
                    } else null
                },
            )

            AnimatedVisibility(result.value != null) {
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = "Результат: ${result.value}",
                    style = TextStyle(fontSize = 18.sp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

fun main() = application {
    Window(title = "Слова в числа", resizable = false, onCloseRequest = ::exitApplication) {
        App()
    }
}
