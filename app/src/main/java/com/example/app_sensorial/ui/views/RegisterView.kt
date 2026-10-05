package com.example.app_sensorial.ui.views

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.app_sensorial.data.User
import com.example.app_sensorial.data.userRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterView(
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var selectedProfile by remember { mutableStateOf("Usuario Sordo / Hipoacúsico") }
    var hearingLevel by remember { mutableStateOf("Sordera profunda") }
    var termsAccepted by remember { mutableStateOf(false) }

    var levelExpanded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val hearingLevels = listOf("Sordera leve", "Sordera moderada", "Sordera severa", "Sordera profunda", "Cofosis")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registro de Usuario", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre completo") },
            enabled = !isLoading,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Nombre de usuario") },
            enabled = !isLoading,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = "Mostrar/Ocultar")
                }
            },
            enabled = !isLoading,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar contraseña") },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = "Mostrar/Ocultar")
                }
            },
            enabled = !isLoading,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Selecciona tu perfil:", modifier = Modifier.align(Alignment.Start))
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedProfile == "Usuario Sordo / Hipoacúsico",
                    onClick = { selectedProfile = "Usuario Sordo / Hipoacúsico" }
                )
                Text("Usuario Sordo / Hipoacúsico")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedProfile == "Usuario Oyente",
                    onClick = { selectedProfile = "Usuario Oyente" }
                )
                Text("Usuario Oyente")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (selectedProfile == "Usuario Sordo / Hipoacúsico") {
            ExposedDropdownMenuBox(
                expanded = levelExpanded,
                onExpandedChange = { if (!isLoading) levelExpanded = !levelExpanded }
            ) {
                OutlinedTextField(
                    value = hearingLevel,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Nivel Auditivo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = levelExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = levelExpanded,
                    onDismissRequest = { levelExpanded = false }
                ) {
                    hearingLevels.forEach { level ->
                        DropdownMenuItem(
                            text = { Text(level) },
                            onClick = {
                                hearingLevel = level
                                levelExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Checkbox(
                checked = termsAccepted,
                onCheckedChange = { termsAccepted = it },
                enabled = !isLoading
            )
            Text("Acepto los términos de accesibilidad")
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "La contraseña debe tener mín. 6 caracteres, una mayúscula, una minúscula y un número.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(8.dp))
        } else {
            Button(
                onClick = {
                    when {
                        name.isBlank() || username.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                            errorMessage = "Por favor, completa todos los campos."
                        }
                        !termsAccepted -> {
                            errorMessage = "Debes aceptar los términos de accesibilidad."
                        }
                        password.length < 6 || !password.any { it.isUpperCase() } || !password.any { it.isLowerCase() } || !password.any { it.isDigit() } -> {
                            errorMessage = "La contraseña debe tener mín. 6 caracteres, una mayúscula, una minúscula y un número."
                        }
                        password != confirmPassword -> {
                            errorMessage = "Las contraseñas no coinciden."
                        }
                        userRepository.userExists(username.trim()) -> {
                            errorMessage = "El nombre de usuario ya está en uso."
                        }
                        else -> {
                            isLoading = true
                            errorMessage = ""
                            val finalHearingLevel = if (selectedProfile == "Usuario Oyente") "N/A" else hearingLevel

                            val newUser = User(
                                name = name.trim(),
                                username = username.trim(),
                                profile = selectedProfile,
                                hearingLevel = finalHearingLevel
                            )

                            userRepository.registerUserWithFirebase(newUser, password) { success, message ->
                                isLoading = false
                                if (success) {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                    onRegisterSuccess()
                                } else {
                                    errorMessage = message
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarme")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onNavigateBack, enabled = !isLoading) {
            Text("Volver al Login")
        }
    }
}