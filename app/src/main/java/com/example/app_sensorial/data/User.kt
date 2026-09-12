package com.example.app_sensorial.data

data class User(
    val fullName: String,
    val username: String,
    val pass: String,
    val profileType: String
)

class UserRepository {
    private val registeredUsers: Array<User?> = arrayOfNulls(5)

    init {
        registeredUsers[0] = User("Usuario Test 1", "test1", "Test1234", "Usuario Sordo / Hipoacúsico")
        registeredUsers[1] = User("Usuario Test 2", "test2", "Test1234", "Usuario Oyente")
        registeredUsers[2] = User("Usuario Test 3", "test3", "Test1234", "Usuario Sordo / Hipoacúsico")
    }

    fun registerUser(newUser: User): Pair<Boolean, String> {
        val exists = registeredUsers.filterNotNull().any { it.username == newUser.username }
        if (exists) {
            return Pair(false, "El nombre de usuario ya está registrado.")
        }

        val emptyIndex = registeredUsers.indexOfFirst { it == null }

        if (emptyIndex != -1) {
            registeredUsers[emptyIndex] = newUser
            return Pair(true, "Registro exitoso.")
        } else {
            return Pair(false, "Límite máximo de 5 usuarios alcanzado.")
        }
    }

    fun validateLogin(username: String, pass: String): Boolean {
        return registeredUsers.filterNotNull().any { it.username == username && it.pass == pass }
    }

    fun userExists(username: String): Boolean {
        return registeredUsers.filterNotNull().any { it.username == username }
    }
}

val userRepository = UserRepository()