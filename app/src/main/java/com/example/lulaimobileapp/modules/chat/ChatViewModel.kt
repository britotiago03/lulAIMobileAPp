package com.example.lulaimobileapp.modules.chat

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class ChatViewModel : ViewModel() {
    val messages = mutableStateListOf<String>()

    fun sendMessage(message: String) {
        messages.add("You: $message")
        // Simulate chatbot response
        messages.add("LulAI: Here's your answer!")
    }
}