package com.example.app_sensorial.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class RegistroHistorial(val texto: String = "", val tipo: String = "")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialView(onNavigateBack: () -> Unit) {
    var historialList by remember { mutableStateOf<List<RegistroHistorial>>(emptyList()) }
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    // Sincronización en tiempo real con Firebase Realtime Database
    DisposableEffect(userId) {
        if (userId != null) {
            val databaseRef = FirebaseDatabase.getInstance().getReference("historial").child(userId)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val lista = mutableListOf<RegistroHistorial>()
                    for (hijo in snapshot.children) {
                        val registro = hijo.getValue(RegistroHistorial::class.java)
                        if (registro != null) lista.add(registro)
                    }
                    // Ordena los resultados, mostrando los más recientes arriba
                    historialList = lista.reversed()
                }
                override fun onCancelled(error: DatabaseError) {}
            }
            databaseRef.addValueEventListener(listener)
            onDispose { databaseRef.removeEventListener(listener) }
        } else {
            onDispose { }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Historial") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver al menú")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (historialList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Aún no hay registros en tu historial.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(historialList) { registro ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = registro.tipo.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = registro.texto, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}