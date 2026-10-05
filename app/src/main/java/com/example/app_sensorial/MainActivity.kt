package com.example.app_sensorial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app_sensorial.ui.views.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "login") {

                        // LOGIN
                        composable("login") {
                            LoginView(
                                onLoginSuccess = { username ->
                                    navController.navigate("home/$username") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToRegister = { navController.navigate("register") },
                                onNavigateToRecovery = { navController.navigate("recovery") }
                            )
                        }

                        // REGISTRO
                        composable("register") {
                            RegisterView(
                                onRegisterSuccess = { navController.popBackStack() },
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        // RECUPERACIÓN
                        composable("recovery") {
                            PassRecoveryView(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        // HOME MENÚ
                        composable("home/{username}") { backStackEntry ->
                            val username = backStackEntry.arguments?.getString("username") ?: "Usuario"
                            HomeMenuView(
                                username = username,
                                onNavigateToEscribir = { navController.navigate("escribir") },
                                onNavigateToHablar = { navController.navigate("hablar") },
                                onNavigateToBuscar = { navController.navigate("buscar") },
                                onNavigateToLogin = {
                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }

                        // ESCRIBIR (Text to Speech)
                        composable("escribir") {
                            EscribirView(onNavigateBack = { navController.popBackStack() })
                        }

                        // HABLAR (Speech to Text)
                        composable("hablar") {
                            HablarView(onNavigateBack = { navController.popBackStack() })
                        }

                        // BUSCAR DISPOSITIVO (Geolocalización)
                        composable("buscar") {
                            BuscarDispositivoView(onNavigateBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}