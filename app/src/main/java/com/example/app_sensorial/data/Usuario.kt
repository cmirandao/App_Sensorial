package com.example.app_sensorial.data

data class Usuario(
    val nombreCompleto: String,
    val nombreUsuario: String,
    val contrasena: String,
    val nivelAuditivo: String
)

// Usuarios iniciales
val listaUsuarios = mutableListOf(
    Usuario("Sofía Morales", "sofia_m", "Clave123", "Hipoacusia leve"),
    Usuario("Victoria Morales", "vicky_m", "Clave456", "Oyente (Familiar)"),
    Usuario("Carlos Ruiz", "carlos_r", "Clave789", "Sordera profunda"),
    Usuario("Ana Silva", "ana_s", "Clave321", "Hipoacusia moderada"),
    Usuario("Luis Torres", "luis_t", "Clave654", "Sordera total")
)