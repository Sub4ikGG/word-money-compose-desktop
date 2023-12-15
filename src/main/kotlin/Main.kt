import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
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

        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Column(
                modifier = Modifier.padding(16.dp).width(IntrinsicSize.Max),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))

                OutlinedTextField(
                    modifier = Modifier.padding(top = 8.dp),
                    value = input.value,
                    onValueChange = {
                        input.value = it
                    },
                    textStyle = TextStyle(fontSize = 20.sp),
                    placeholder = { Text(text = "Слова", style = TextStyle(fontSize = 20.sp)) }
                )

                Button(
                    modifier = Modifier.fillMaxWidth().height(60.dp).padding(top = 8.dp),
                    content = {
                        Text(text = "Посчитать")
                    },
                    onClick = {
                        result.value = if (input.value.isNotBlank()) {
                            try {
                                convertWordToInt(word = input.value).toString()
                            } catch (e: Exception) {
                                e.localizedMessage
                            }
                        } else null
                    }
                )

                AnimatedVisibility(result.value != null) {
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = "Итог: ${result.value}",
                        style = TextStyle(fontSize = 18.sp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

fun main() = application {
    Window(title = "Влад Горбушин, домашняя работа №1", resizable = false, onCloseRequest = ::exitApplication) {
        App()
    }
}
