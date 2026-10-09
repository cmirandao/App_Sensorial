package com.example.app_sensorial.ui.views

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialView(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var isTtsReady by remember { mutableStateOf(false) }

    val tutorialText = """
        Bienvenido al tutorial de App Sensorial. 
        Para escribir y que el teléfono hable por ti, selecciona la opción 'Escribir' en el menú principal. Escribe tu frase y presiona el botón de reproducción.
        Para escuchar a otra persona y leer lo que dice, selecciona 'Hablar'. El micrófono capturará la voz y la convertirá en texto.
        Para ver tu historial de frases guardadas, selecciona 'Historial'.
    """.trimIndent()

    DisposableEffect(context) {
        val textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.Builder().setLanguage("es").setRegion("CL").build()
                isTtsReady = true
            }
        }
        tts = textToSpeech

        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tutorial de Uso") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (isTtsReady) {
                        tts?.speak(tutorialText, TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Escuchar tutorial", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Guía rápida de la aplicación",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            TutorialCard(
                titulo = "1. Función Escribir (Texto a Voz)",
                descripcion = "Escribe una frase en el cuadro de texto y presiona reproducir para que el dispositivo hable por ti."
            )

            TutorialCard(
                titulo = "2. Función Hablar (Voz a Texto)",
                descripcion = "Presiona el micrófono para capturar la voz de otra persona. La aplicación escribirá lo que escuche en la pantalla."
            )

            TutorialCard(
                titulo = "3. Historial",
                descripcion = "Todas las frases que generes se guardarán automáticamente en tu historial personal."
            )

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
fun TutorialCard(titulo: String, descripcion: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = titulo, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = descripcion, style = MaterialTheme.typography.bodyMedium)
        }
    }
}