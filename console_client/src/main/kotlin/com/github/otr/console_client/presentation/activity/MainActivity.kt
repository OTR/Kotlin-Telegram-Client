package com.github.otr.console_client.presentation.activity

import com.github.otr.console_client.domain.entity.AuthState
import com.github.otr.console_client.presentation.viewmodel.AuthViewModel

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.StateFlow

/**
 * A fake Main Activity to test presentation layer
 */
object MainActivity {

    /**
     *
     */
    fun onCreate() {

        val authViewModel: AuthViewModel = AuthViewModel()
        val authStateFlow: StateFlow<AuthState> = authViewModel.authState
        val str: AuthState = authStateFlow.value
        println("Hello, initial value of: $str!")

        runBlocking {
            authStateFlow.collect {
                println("Hello, next value of: $str!")
            }
        }
    }

}

fun main() {
    MainActivity.onCreate()
}
