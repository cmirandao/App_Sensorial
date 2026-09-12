package com.example.app_sensorial.utils

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import java.util.Locale

fun Context.iniciarReconocimientoSeguro(
    launchAction: (Intent) -> Unit,
    onError: (Exception) -> Unit
) {
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora para transcribir...")
    }

    // Manejo de errores
    try {
        launchAction(intent)
    } catch (e: Exception) {
        onError(e)
    }
}