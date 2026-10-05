package com.example.app_sensorial

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.app_sensorial.ui.views.LoginView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun probarQueElBotonIngresarApareceEnPantalla() {
        // Arrange: Vista de Login aislada
        composeTestRule.setContent {
            LoginView(
                onLoginSuccess = {},
                onNavigateToRegister = {},
                onNavigateToRecovery = {}
            )
        }

        // Act & Assert: confirmar que el botón ingresar es visible
        composeTestRule.onNodeWithText("Ingresar").assertIsDisplayed()
    }
}