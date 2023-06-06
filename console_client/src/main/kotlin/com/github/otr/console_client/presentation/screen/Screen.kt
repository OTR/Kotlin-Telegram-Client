package com.github.otr.console_client.presentation.screen

/**
 *
 */
abstract class Screen {

    internal abstract val menuItems: List<Triple<String, String, () -> Unit>>

    open protected fun getMenu(): String {
        return menuItems.joinToString("\n") { "${it.first}. ${it.second}" }
    }

    fun display() {
        while (true) {
            println(getMenu())
            print(">>> ")
            val userInput: String = readln()
            menuItems.find { it.first == userInput }?.third?.invoke()
        }
    }

}
