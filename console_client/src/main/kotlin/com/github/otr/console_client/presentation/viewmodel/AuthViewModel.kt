package com.github.otr.console_client.presentation.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 *
 */
class AuthViewModel {

    private val _authState: MutableStateFlow<String> = MutableStateFlow("")
    val authState: StateFlow<String> = _authState.asStateFlow()

}
