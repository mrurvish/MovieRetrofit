package com.example.movieretrofit.translator

import android.util.Log
import com.example.movieretrofit.model.restFoodApi
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

class Translator {
    private val translatorEnRu: com.google.mlkit.nl.translate.Translator
    private val translatorRuEn: com.google.mlkit.nl.translate.Translator

    init {
        val optionsEnRu = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.ENGLISH)
            .build()
        val optionsRuEn = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.RUSSIAN)
            .setTargetLanguage(TranslateLanguage.ENGLISH)
            .build()
        translatorEnRu = Translation.getClient(optionsEnRu)
        translatorRuEn = Translation.getClient(optionsRuEn)

        val conditions = DownloadConditions.Builder().requireWifi().build()
        translatorEnRu.downloadModelIfNeeded(conditions)
        translatorRuEn.downloadModelIfNeeded(conditions)
    }

    fun translateEnRu(text: String, onSuccess: (String) -> Unit) {
        translate(translatorEnRu, text) { onSuccess(it) }
    }

    fun translateRuEn(text: String, onSuccess: (String) -> Unit) {
        translate(translatorRuEn, text) { onSuccess(it) }
    }

    fun translateRuEn2(text: String): Deferred<String> {
        val deferred = CompletableDeferred<String>()

        translate(translatorRuEn, text) { translatedText ->
            deferred.complete(translatedText)
        }
        return deferred
    }

    private fun translate(
        translator: com.google.mlkit.nl.translate.Translator,
        text: String,
        onSuccess: (String) -> Unit
    ) {
        translator.translate(text).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val translatedText = task.result
                onSuccess(translatedText) // вызываем функцию-обработчик успеха
            } else {
                Log.e("MyLog", "Ошибка перевода ${task.exception}")
            }
        }
    }
}