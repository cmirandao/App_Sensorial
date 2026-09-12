package com.example.app_sensorial.ui.views

import android.app.Activity
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.util.Locale

import com.example.app_sensorial.utils.iniciarReconocimientoSeguro

@Composable
fun ComunicadorView(
    onLogout: () -> Unit
) {
    var textoEscrito by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    // ==========================================
    // Text-to-Speech
    // ==========================================
    val tts = remember(context) {
        var textToSpeech: TextToSpeech? = null
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.forLanguageTag("es-ES")
            }
        }
        textToSpeech
    }

    DisposableEffect(Unit) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    // ==========================================
    // Speech-to-Text
    // ==========================================
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (!spokenText.isNullOrEmpty()) {
                val espacio = if (textoEscrito.isNotEmpty() && !textoEscrito.endsWith(" ")) " " else ""
                textoEscrito += espacio + spokenText
                errorMessage = ""
            }
        }
    }

    val frasesRapidas = listOf(
        "Hola", "Gracias", "Por favor", "Sí", "No",
        "Necesito ayuda", "¿Puedes repetirlo?", "No te escucho",
        "Soy sordo/a", "Escríbelo por favor"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {

            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Comunicador Accesible",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 16.dp, top = 24.dp)
                    )

                    OutlinedTextField(
                        value = textoEscrito,
                        onValueChange = { textoEscrito = it },
                        label = { Text("Mensaje a comunicar...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        maxLines = 4
                    )

                    if (errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = {
                            if (textoEscrito.isNotEmpty()) {
                                tts.speak(textoEscrito, TextToSpeech.QUEUE_FLUSH, null, null)
                            }
                        }) {
                            Text("Reproducir Voz")
                        }

                        Button(onClick = {
                            context.iniciarReconocimientoSeguro(
                                launchAction = { intent ->
                                    speechLauncher.launch(intent)
                                },
                                onError = { exception ->
                                    errorMessage = "Error al iniciar micrófono: Verifica tu conexión a internet."
                                }
                            )
                        }) {
                            Text("Dictar")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(onClick = {
                        textoEscrito = ""
                        errorMessage = ""
                    }) {
                        Text("Borrar todo")
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Toca una frase para agregarla:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
                    )
                }
            }

            items(frasesRapidas) { frase ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val espacio = if (textoEscrito.isNotEmpty() && !textoEscrito.endsWith(" ")) " " else ""
                            textoEscrito += espacio + frase
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = frase,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = onLogout,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Cerrar Sesión")
                    }
                }
            }
        }
    }
}