package com.github.otr.console_client.presentation.activity

import com.github.otr.console_client.domain.entity.AuthState
import com.github.otr.console_client.presentation.screen.MainScreen
import com.github.otr.console_client.presentation.viewmodel.AuthViewModel

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.StateFlow

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException

/**
 * A fake Main Activity to test presentation layer
 */
private object MainActivity {

    private const val WAIT_UNTIL_LOGIN: Long = 1000L

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

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
        try {
            observeViewModel()
        } catch (e: IOException) {
            logger.error(e.message)
            throw e
        } catch (e: UnsupportedClassVersionError) {
            logger.error("Use Java version 17")
            logger.error(e.message)
            throw e
        }
    }

    /**
     * Subscribe to Auth State Flow, and launch different Screens depend on State value
     */
    private fun observeViewModel() {

        val authStateFlow: StateFlow<AuthState> = authViewModel.authStateFlow

        runBlocking {
            delay(WAIT_UNTIL_LOGIN) // Wait until successful login
            authStateFlow.collect {
                when(it) {
                    AuthState.INITIAL -> { println("INITIAL") }
                    AuthState.CLOSED -> { println("CLOSED") }
                    AuthState.CLOSING -> { println("CLOSING") }
                    AuthState.LOGGING_OUT -> { println("LOGGING_OUT") }
                    AuthState.READY -> { MainScreen(authViewModel).display() }
                    AuthState.WAIT_CODE -> {
                        print("TODO: Display a Screen ")
                        println("where asking the User for a verification code")
                        val verificationCode: String = authViewModel.getVerificationCode()
                        authViewModel.setVerificationCode(verificationCode)
                    }
                    AuthState.WAIT_EMAIL_ADDRESS -> { println("WAIT_EMAIL_ADDRESS") }
                    AuthState.WAIT_EMAIL_CODE -> { println("WAIT_EMAIL_CODE") }
                    AuthState.WAIT_OTHER_DEVICE_CONFIRMATION -> { println("WAIT_OTHER_DEVICE") }
                    AuthState.WAIT_PASSWORD -> { println("WAIT_PASSWORD") }
                    AuthState.WAIT_PHONE_NUMBER -> {
                        println("TODO: Display a Screen where asking the User for a phone number")
                        val phoneNumber: String = authViewModel.getPhoneNumber()
                        authViewModel.setPhoneNumber(phoneNumber)
                    }
                    AuthState.WAIT_REGISTRATION -> { println("WAIT_REGISTRATION") }
                    AuthState.WAIT_TD_LIB_PARAMETERS -> { println("WAIT_TD_LIB_PARAMETERS") }

                    // FIXME: Replace with sealed classes
                    AuthState.ERROR -> {
                        when(it.message) {
                            "PHONE_CODE_INVALID" -> { println("PHONE_CODE_INVALID") }
                        }
                    }
                    //
                    AuthState.SEND_PHONE_NUMBER -> {
                        logger.trace("SEND_PHONE_NUMBER ${it.message}")
                    }
                    //
                    AuthState.SEND_VERIFICATION_CODE -> {
                        logger.trace("SEND_VERIFICATION_CODE ${it.message}")
                    }
                }
            }
        }

    }

}

fun main() {
    MainActivity.onCreate()
}
