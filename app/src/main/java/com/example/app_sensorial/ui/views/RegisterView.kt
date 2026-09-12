package com.example.app_sensorial.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var disclaimer by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Opciones del Radio Button
    val profileOptions = listOf("Usuario Sordo / Hipoacúsico", "Usuario Oyente")
    var selectedProfile by remember { mutableStateOf(profileOptions[0]) }

    // Opciones del Combo Box
    val hearingLevels = listOf("Audición Normal", "Sordera Leve", "Sordera Moderada", "Sordera Severa", "Sordera Profunda")
    var expandedLevel by remember { mutableStateOf(false) }
    var selectedLevel by remember { mutableStateOf(hearingLevels[4]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registro de Usuario", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre completo") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Nombre de usuario") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password, onValueChange = { password = it }, label = { Text("Contraseña") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, contentDescription = null)
                }
            }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirmar contraseña") },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, contentDescription = null)
                }
            }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Selecciona tu perfil:", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.align(Alignment.Start))
        Column(Modifier.selectableGroup().fillMaxWidth()) {
            profileOptions.forEach { profile ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = (profile == selectedProfile), onClick = { selectedProfile = profile })
                    Text(text = profile)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expandedLevel,
            onExpandedChange = { expandedLevel = !expandedLevel },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedLevel, onValueChange = {}, readOnly = true, label = { Text("Nivel Auditivo") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLevel) },
                modifier = Modifier.menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expandedLevel, onDismissRequest = { expandedLevel = false }) {
                hearingLevels.forEach { level ->
                    DropdownMenuItem(text = { Text(level) }, onClick = { selectedLevel = level; expandedLevel = false })
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Checkbox(checked = disclaimer, onCheckedChange = { disclaimer = it })
            Text("Acepto los términos de accesibilidad")
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                when {
                    name.isBlank() || username.isBlank() || password.isBlank() -> errorMessage = "Todos los campos son obligatorios."
                    password.length < 6 || !password.any { it.isUpperCase() } || !password.any { it.isLowerCase() } || !password.any { it.isDigit() } ->
                        errorMessage = "La contraseña debe tener mín. 6 caracteres, una mayúscula, una minúscula y un número."
                    password != confirmPassword -> errorMessage = "Las contraseñas no coinciden."
                    !disclaimer -> errorMessage = "Debes aceptar los términos y condiciones."
                    else -> {
                        val newUser = User(name, username, password, selectedProfile)
                        val (success, message) = userRepository.registerUser(newUser)

                        if (success) {
                            errorMessage = ""
                            onRegisterSuccess()
                        } else {
                            errorMessage = message
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarme")
        }

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(onClick = onNavigateBack, modifier = Modifier.fillMaxWidth()) {
            Text("Volver al Login")
        }
    }
}