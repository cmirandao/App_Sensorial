package com.example.app_sensorial.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

data class User(
    val name: String = "",
    val username: String = "",
    val profile: String = "",
    val hearingLevel: String = ""
)

class UserRepository {
    private val auth: FirebaseAuth = Firebase.auth
    private val database: DatabaseReference = Firebase.database.getReference("users")
    private val registeredUsers = mutableListOf<User>()

    init {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                registeredUsers.clear()
                for (child in snapshot.children) {
                    val user = child.getValue(User::class.java)
                    if (user != null) {
                        registeredUsers.add(user)
                    }
                }
                Log.d("UserRepository", "Usuarios sincronizados: ${registeredUsers.size}")
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("UserRepository", "Error al leer usuarios", error.toException())
            }
        })
    }

    private fun sanitizeKey(username: String): String {
        return username.trim().replace(".", "_").replace("#", "_").replace("$", "_").replace("[", "_").replace("]", "_")
    }

    private fun formatAuthEmail(username: String): String {
        val clean = username.trim()
        return if (clean.contains("@")) clean else "$clean@appsensorial.cl"
    }

    fun registerUserWithFirebase(user: User, pass: String, onResult: (Boolean, String) -> Unit) {
        val emailForAuth = formatAuthEmail(user.username)
        auth.createUserWithEmailAndPassword(emailForAuth, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val key = sanitizeKey(user.username)
                    database.child(key).setValue(user)
                        .addOnSuccessListener { onResult(true, "Usuario registrado exitosamente.") }
                        .addOnFailureListener { e -> onResult(false, "Error al guardar perfil: ${e.localizedMessage}") }
                } else {
                    onResult(false, task.exception?.localizedMessage ?: "Error de registro")
                }
            }
    }

    fun loginWithFirebase(username: String, pass: String, onResult: (Boolean, String) -> Unit) {
        val emailForAuth = formatAuthEmail(username)
        auth.signInWithEmailAndPassword(emailForAuth, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, "Autenticación exitosa")
                } else {
                    onResult(false, "Credenciales incorrectas")
                }
            }
    }

    fun sendPasswordReset(username: String, onResult: (Boolean, String) -> Unit) {
        val emailForAuth = formatAuthEmail(username)
        auth.sendPasswordResetEmail(emailForAuth)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, "Instrucciones enviadas al correo.")
                } else {
                    onResult(false, task.exception?.localizedMessage ?: "Error al enviar correo.")
                }
            }
    }

    // Eliminar cuenta de usuario
    fun eliminarCuenta(username: String, onResult: (Boolean, String) -> Unit) {
        val userAuth = auth.currentUser
        if (userAuth != null) {
            val key = sanitizeKey(username)
            database.child(key).removeValue().addOnCompleteListener { dbTask ->
                if (dbTask.isSuccessful) {
                    // Borrar historial
                    Firebase.database.getReference("historial").child(userAuth.uid).removeValue()
                    // Eliminar autenticación
                    userAuth.delete().addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            onResult(true, "Cuenta eliminada correctamente")
                        } else {
                            onResult(false, "Error al eliminar Auth: ${authTask.exception?.localizedMessage}")
                        }
                    }
                } else {
                    onResult(false, "Error al eliminar datos BD: ${dbTask.exception?.localizedMessage}")
                }
            }
        } else {
            onResult(false, "No hay sesión activa.")
        }
    }

    fun signOut() {
        auth.signOut()
    }

    fun userExists(username: String): Boolean {
        return registeredUsers.any { it.username == username }
    }
}

val userRepository = UserRepository()