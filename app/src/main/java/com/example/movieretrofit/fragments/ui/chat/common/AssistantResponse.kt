package com.example.movieretrofit.fragments.ui.chat.common

import android.util.Log
import com.example.movieretrofit.fragments.ui.chat.MessageAdapter
import com.example.movieretrofit.fragments.ui.chat.common.Constants.OPEN_GOOGLE
import com.example.movieretrofit.fragments.ui.chat.common.Constants.OPEN_SEARCH
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

object BotResponse {
    fun basicResponses(_message: String): String {
        val random = (0..2).random()
        val message = _message.toLowerCase()

        return when {
            message.contains("add") -> {
                val food: String = message.toLowerCase().substringAfterLast("add ")
                Log.e("response", "food is ${food}")

                "Product $food added"
            }

            message.contains("how much") && (message.contains("calories") || message.contains("kcal")) -> {
                val food: String = message.toLowerCase().substringAfterLast("in ")
                "In product $food calories:"
            }

            message.contains("how much") && (message.contains("protein")) -> {
                val food: String = message.toLowerCase().substringAfterLast("in ")
                "In product $food protein:"
            }

            message.contains("how much") && (message.contains("fat")) -> {
                val food: String = message.toLowerCase().substringAfterLast("in ")
                "In product $food fat:"
            }

            message.contains("how much") && (message.contains("carbohydrates")) -> {
                val food: String = message.toLowerCase().substringAfterLast("in ")
                "In product $food carbohydrates:"
            }

            message.contains("how much") && (message.contains("macronutrients")) -> {
                val food: String = message.toLowerCase().substringAfterLast("in ")
                "In product $food macronutrients:"
            }

            // Flips a coin
            message.contains("flip") && message.contains("coin") -> {
                val r = (0..1).random()
                val result = if (r == 0) "heads" else "tails"

                "I flipped a coin, and it landed on $result"
            }

            // Math calculations
            message.contains("solve") -> {
                val equation: String? = message.substringAfterLast("solve")
                return try {
                    val answer = SolveMath.solveMath(equation ?: "0")
                    "$answer"

                } catch (e: Exception) {
                    "Sorry, I can't solve that"
                }
            }

            // Hello
            message.contains("Hello") -> {
                when (random) {
                    0 -> "Hello!"
                    1 -> "Sup"
                    2 -> "Buongiorno!"
                    else -> "error"
                }
            }

            // What time is it?
            message.contains("time") && message.contains("?") -> {
                val timeStamp = Timestamp(System.currentTimeMillis())
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val date = sdf.format(Date(timeStamp.time))

                date.toString()
            }

            // Open Google
            message.contains("open") && message.contains("google") -> {
                OPEN_GOOGLE
            }

            // Search on the internet
            message.contains("search") -> {
                OPEN_SEARCH
            }

            // When the program doesn't understand...
            else -> {
                when (random) {
                    0 -> "I don't understand..."
                    1 -> "Try asking me something different"
                    2 -> "Idk"
                    else -> "error"
                }
            }
        }
    }
}
