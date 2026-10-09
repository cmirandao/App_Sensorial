package com.example.app_sensorial.ui.views

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.app_sensorial.data.userRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeMenuView(
    username: String,
    onNavigateToEscribir: () -> Unit,
    onNavigateToHablar: () -> Unit,
    onNavigateToBuscar: () -> Unit,
    onNavigateToHistorial: () -> Unit,
    onNavigateToTutorial: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Estado para guardar el nombre real que viene de la base de datos
    var nombreUsuario by remember { mutableStateOf("Usuario") }

    // Consultar el nombre real desde Firebase Realtime Database
    DisposableEffect(username) {
        val sanitizedKey = username.trim().replace(".", "_").replace("#", "_").replace("$", "_").replace("[", "_").replace("]", "_")
        val nameRef = FirebaseDatabase.getInstance().getReference("users").child(sanitizedKey).child("name")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val nameInDb = snapshot.getValue(String::class.java)
                if (!nameInDb.isNullOrBlank()) {
                    nombreUsuario = nameInDb
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        nameRef.addValueEventListener(listener)

        onDispose {
            nameRef.removeEventListener(listener)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("App Sensorial") },
                actions = {
                    TextButton(onClick = {
                        userRepository.signOut()
                        onNavigateToLogin()
                    }) {
                        Text("Cerrar Sesión")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¡Hola, $nombreUsuario!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "¿Qué herramienta necesitas hoy?",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = onNavigateToEscribir, modifier = Modifier.fillMaxWidth().height(60.dp)) {
                Text("Escribir (Texto a Voz)")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onNavigateToHablar, modifier = Modifier.fillMaxWidth().height(60.dp)) {
                Text("Escuchar (Voz a Texto)")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onNavigateToBuscar, modifier = Modifier.fillMaxWidth().height(60.dp)) {
                Text("Buscar Dispositivo (GPS)")
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(onClick = onNavigateToHistorial, modifier = Modifier.fillMaxWidth().height(60.dp)) {
                Text("Ver Mi Historial")
            }
            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToTutorial) {
                Text("¿Cómo usar la aplicación? (Tutorial)")
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedButton(
                onClick = { showDeleteDialog = true },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Eliminar mi cuenta")
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar cuenta") },
            text = { Text("Esta acción borrará tus datos de forma permanente. ¿Deseas continuar?") },
            confirmButton = {
                Button(
                    onClick = {
                        userRepository.eliminarCuenta(username) { success, message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            if (success) {
                                showDeleteDialog = false
                                onNavigateToLogin()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}