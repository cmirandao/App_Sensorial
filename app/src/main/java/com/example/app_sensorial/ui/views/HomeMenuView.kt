package com.example.app_sensorial.ui.views

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.app_sensorial.data.userRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeMenuView(
    username: String,
    onNavigateToEscribir: () -> Unit,
    onNavigateToHablar: () -> Unit,
    onNavigateToBuscar: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }

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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Hola, $username",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "¿Qué herramienta necesitas hoy?",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onNavigateToEscribir,
                modifier = Modifier.fillMaxWidth().height(60.dp)
            ) {
                Text("Escribir (Texto a Voz)")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigateToHablar,
                modifier = Modifier.fillMaxWidth().height(60.dp)
            ) {
                Text("Escuchar (Voz a Texto)")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigateToBuscar,
                modifier = Modifier.fillMaxWidth().height(60.dp)
            ) {
                Text("Buscar Dispositivo (GPS)")
            }

            Spacer(modifier = Modifier.height(48.dp))

            OutlinedButton(
                onClick = { showDeleteDialog = true },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Eliminar mi cuenta")
            }
        }
    }

    // Confirmación para eliminar cuenta en Firebase
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar cuenta") },
            text = { Text("Esta acción borrará tus datos de forma permanente. ¿Deseas continuar?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        val userAuth = Firebase.auth.currentUser
                        val sanitizedKey = username.trim().replace(".", "_").replace("#", "_").replace("$", "_").replace("[", "_").replace("]", "_")

                        // Eliminar de Realtime Database y luego de Auth
                        Firebase.database.getReference("users").child(sanitizedKey).removeValue().addOnCompleteListener {
                            userAuth?.delete()?.addOnCompleteListener {
                                Toast.makeText(context, "Cuenta eliminada", Toast.LENGTH_SHORT).show()
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