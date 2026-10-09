package com.example.app_sensorial.ui.views

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EscribirView(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    var textToSpeak by remember { mutableStateOf("") }
    var tts: TextToSpeech? by remember { mutableStateOf(null) }

    DisposableEffect(context) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.Builder().setLanguage("es").setRegion("CL").build()
            }
        }
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    fun guardarEnHistorial(texto: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null && texto.isNotBlank()) {
            val ref = FirebaseDatabase.getInstance().getReference("historial").child(userId)
            val registro = mapOf("texto" to texto, "tipo" to "Escribir")
            ref.push().setValue(registro).addOnSuccessListener {
                Toast.makeText(context, "Guardado en historial", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Escribir (Texto a Voz)") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Escribe un mensaje y el dispositivo lo leerá en voz alta para comunicarte con los demás.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = textToSpeak,
                onValueChange = { textToSpeak = it },
                label = { Text("Mensaje a comunicar") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 5
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (textToSpeak.isNotBlank()) {
                        tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null)
                        guardarEnHistorial(textToSpeak)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Reproducir Voz y Guardar")
            }

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = { textToSpeak = "" }, modifier = Modifier.fillMaxWidth()) {
                Text("Borrar todo")
            }
        }
    }
}