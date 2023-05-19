package com.github.otr.console_client.domain.entity

/**
 * @see <a href="https://tdlight-team.github.io/tdlight-docs/tdlight.api/it/tdlight/jni/TdApi.AuthorizationState.html">
 *     Generated docs for `TdApi.AuthorizationState`
 *     </a>
 */
enum class AuthState(var message: String = "") {
    INITIAL,
    ERROR, // FIXME: Replace with sealed classes
    SEND_PHONE_NUMBER,
    SEND_VERIFICATION_CODE,
    CLOSED,
    CLOSING,
    LOGGING_OUT,
    READY,
    WAIT_CODE,
    WAIT_EMAIL_ADDRESS,
    WAIT_EMAIL_CODE,
    WAIT_OTHER_DEVICE_CONFIRMATION,
    WAIT_PASSWORD,
    WAIT_PHONE_NUMBER,
    WAIT_REGISTRATION,
    WAIT_TD_LIB_PARAMETERS,
}
