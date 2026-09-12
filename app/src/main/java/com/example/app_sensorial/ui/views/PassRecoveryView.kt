package com.example.app_sensorial.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app_sensorial.data.userRepository

@Composable
fun PassRecoveryView(
    onNavigateBack: () -> Unit
) {
    var usernameInput by remember { mutableStateOf("") }
    var recoveryMessage by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Recuperar Contraseña", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Ingresa tu usuario y te enviaremos las instrucciones.", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = usernameInput,
            onValueChange = { usernameInput = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (usernameInput.isBlank()) {
                    isError = true
                    recoveryMessage = "Debes ingresar un usuario válido."
                } else {
                    val userExists = userRepository.userExists(usernameInput)

                    if (userExists) {
                        isError = false
                        recoveryMessage = "Instrucciones enviadas con éxito a $usernameInput."
                    } else {
                        isError = true
                        recoveryMessage = "El usuario no existe. Por favor, regístrate."
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar instrucciones")
        }

        if (recoveryMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = recoveryMessage,
                color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(onClick = onNavigateBack, modifier = Modifier.fillMaxWidth()) {
            Text("Volver al Login")
        }
    }
}