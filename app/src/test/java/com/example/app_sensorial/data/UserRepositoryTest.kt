package com.example.app_sensorial.data

import android.os.Build
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

// Simulación con Robolectric, JUnit, Mockito y TDD
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], manifest = Config.NONE)
class UserRepositoryTest {

    @Test
    fun `probar validacion de usuario simulado con Mockito en entorno Robolectric`() {
        // Arrange
        val mockRepository = mock(UserRepository::class.java)
        val testUsername = "carolina_test"

        `when`(mockRepository.userExists(testUsername)).thenReturn(true)

        // Act
        val result = mockRepository.userExists(testUsername)

        // Assert
        assertTrue("El mock debería devolver true", result)
    }
}