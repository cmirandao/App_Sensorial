package com.example.app_sensorial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.app_sensorial.ui.theme.App_SensorialTheme
import com.example.app_sensorial.ui.views.LoginView
import com.example.app_sensorial.ui.views.RegisterView
import com.example.app_sensorial.ui.views.PassRecoveryView
import com.example.app_sensorial.ui.views.ComunicadorView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App_SensorialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavigation()
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginView(
                onLoginSuccess = { navController.navigate("comunicador") },
                onNavigateToRegister = { navController.navigate("registro") },
                onNavigateToRecover = { navController.navigate("recuperar") }
            )
        }

        composable("registro") {
            RegisterView(
                onRegisterSuccess = {
                    navController.popBackStack()
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("recuperar") {
            PassRecoveryView(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("comunicador") {
            ComunicadorView(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
    }
}