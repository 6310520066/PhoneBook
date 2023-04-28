package com.example.phonebook.routing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


sealed class Screen {
    object Contact: Screen()
    object SaveContact: Screen()
    object Trash: Screen()
}

object PhoneContactRouter {
    var currentScreen: Screen by mutableStateOf(Screen.Contact)

    fun navigateTo(destination: Screen) {
        currentScreen = destination
    }
}