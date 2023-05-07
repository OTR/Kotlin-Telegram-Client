package com.github.otr.console_client.presentation.activity

import com.github.otr.console_client.domain.entity.AuthState
import com.github.otr.console_client.presentation.viewmodel.AuthViewModel

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.StateFlow

/**
 * A fake Main Activity to test presentation layer
 */
object MainActivity {

    private val authViewModel: AuthViewModel by lazy {
        AuthViewModel()
    }

    /**
     *
     */
    fun onCreate() {
        // super.onCreate(savedInstanceState)
        // setContentView(...)
        //
        observeViewModel()
    }

    /**
     *
     */
    private fun observeViewModel() {

        val authStateFlow: StateFlow<AuthState> = authViewModel.authStateFlow

        runBlocking {
            delay(1000) // Wait until successful login
            authStateFlow.collect {
                when(it) {
                    AuthState.INITIAL -> { println("INITIAL") }
                    AuthState.CLOSED -> { println("CLOSED") }
                    AuthState.CLOSING -> { println("CLOSING") }
                    AuthState.LOGGING_OUT -> { println("LOGGING_OUT") }
                    AuthState.READY -> { println("READY") }
                    AuthState.WAIT_CODE -> { println("WAIT_CODE") }
                    AuthState.WAIT_EMAIL_ADDRESS -> { println("WAIT_EMAIL_ADDRESS") }
                    AuthState.WAIT_EMAIL_CODE -> { println("WAIT_EMAIL_CODE") }
                    AuthState.WAIT_OTHER_DEVICE_CONFIRMATION -> { println("WAIT_OTHER_DEVICE") }
                    AuthState.WAIT_PASSWORD -> { println("WAIT_PASSWORD") }
                    AuthState.WAIT_PHONE_NUMBER -> {
//                        val phoneNumber: String = readln()
                        println("WAIT_PHONE_NUMBER")
//                        authViewModel.setPhoneNumber(phoneNumber)
                    }
                    AuthState.WAIT_REGISTRATION -> { println("WAIT_REGISTRATION") }
                    AuthState.WAIT_TD_LIB_PARAMETERS -> { println("WAIT_TD_LIB_PARAMETERS") }
                }
            }
        }

    }

}

fun main() {
    MainActivity.onCreate()
}
