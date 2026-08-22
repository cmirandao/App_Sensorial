package com.example.app_sensorial.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.input.VisualTransformation
import com.example.app_sensorial.data.Usuario
import com.example.app_sensorial.data.listaUsuarios

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

    // Check list
    var acceptTerms by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Radio Buttons
    val roles = listOf("Usuario Sordo / Hipoacúsico", "Usuario Oyente")
    var selectedRole by remember { mutableStateOf(roles[0]) }

    // Combo box (ExposedDropdownMenuBox)
    var expanded by remember { mutableStateOf(false) }
    val niveles = listOf("Sordera profunda", "Hipoacusia severa", "Hipoacusia moderada", "Hipoacusia leve", "Ninguna")
    var selectedNivel by remember { mutableStateOf(niveles[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registro de Usuario", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre completo") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Nombre de usuario") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = "Mostrar")
                }
            },
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
                    Icon(imageVector = image, contentDescription = "Alternar visibilidad")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Radio Buttons
        Text("Selecciona tu perfil:", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.align(Alignment.Start))
        Column(Modifier.selectableGroup().fillMaxWidth()) {
            roles.forEach { rol ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = (rol == selectedRole),
                        onClick = {
                            selectedRole = rol
                            if (rol == roles[1]) {
                                selectedNivel = "Ninguna"
                            } else {
                                selectedNivel = niveles[0]
                            }
                        }
                    )
                    Text(text = rol)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Combo Box
        if (selectedRole == roles[0]) {
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = selectedNivel,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Nivel Auditivo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    niveles.forEach { nivel ->
                        if (nivel != "Ninguna") {
                            DropdownMenuItem(
                                text = { Text(nivel) },
                                onClick = { selectedNivel = nivel; expanded = false }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Check List
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Checkbox(checked = acceptTerms, onCheckedChange = { acceptTerms = it })
            Text("Acepto los términos de accesibilidad")
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                if (name.isBlank() || username.isBlank() || password.isBlank()) {
                    errorMessage = "Todos los campos son obligatorios"
                } else if (password.length < 6 || !password.any { it.isUpperCase() } || !password.any { it.isLowerCase() } || !password.any { it.isDigit() }) {
                    errorMessage = "La contraseña debe tener mín. 6 caracteres, una mayúscula, una minúscula y un número"
                } else if (password != confirmPassword) {
                    errorMessage = "Las contraseñas no coinciden"
                } else if (!acceptTerms) {
                    errorMessage = "Debes aceptar los términos"
                } else {
                    errorMessage = ""
                    listaUsuarios.add(Usuario(name, username, password, selectedNivel))
                    onRegisterSuccess()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarme")
        }

        TextButton(onClick = onNavigateBack) {
            Text("Volver al Login")
        }
    }
}
