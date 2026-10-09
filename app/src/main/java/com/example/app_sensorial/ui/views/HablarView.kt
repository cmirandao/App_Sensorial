package com.example.app_sensorial.ui.views

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
fun HablarView(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    var recognizedText by remember { mutableStateOf("Toca el botón y pídele a la otra persona que hable...") }

    fun guardarEnHistorial(texto: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val ref = FirebaseDatabase.getInstance().getReference("historial").child(userId)
            val registro = mapOf("texto" to texto, "tipo" to "Hablar")
            ref.push().setValue(registro).addOnSuccessListener {
                Toast.makeText(context, "Voz guardada en historial", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val capturado = data?.get(0)
            if (!capturado.isNullOrBlank()) {
                recognizedText = capturado
                guardarEnHistorial(capturado)
            } else {
                recognizedText = "No se reconoció el texto."
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Escuchar (Voz a Texto)") },
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
                text = "Esta herramienta transcribirá lo que la otra persona diga para que puedas leerlo.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(modifier = Modifier.padding(16.dp).fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = recognizedText, style = MaterialTheme.typography.headlineSmall)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                        putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora para transcribir...")
                    }
                    try {
                        speechRecognizerLauncher.launch(intent)
                    } catch (e: Exception) {
                        recognizedText = "El reconocimiento de voz no está disponible en este dispositivo."
                    }
                },
                modifier = Modifier.fillMaxWidth().height(60.dp)
            ) {
                Text("Presionar para Escuchar")
            }
        }
    }
}