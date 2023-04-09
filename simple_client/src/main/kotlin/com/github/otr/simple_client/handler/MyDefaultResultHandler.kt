package com.github.otr.simple_client.handler

import it.tdlight.common.ResultHandler
import it.tdlight.client.TelegramError
import it.tdlight.jni.TdApi

/**
 * Pass this Handler whenever you need to handle Result
 * which could be `TdApi.Error` in case of negative result
 * or `TdApi.Ok` in case of positive result
 * resultHandler – Result handler with onResult method
 * which will be called with result of the query
 * or with TdApi.Error as parameter. If it is null, nothing will be called.
 */
object MyDefaultResultHandler : ResultHandler<TdApi.Ok>{

    /**
     * Default result handler
     * if `TdApi.Error` returned instead of `TdApi.Ok` on our request - just throw Exception
     */
    override fun onResult(ok: TdApi.Object?) {
        if (ok is TdApi.Error) {
            val error = ok
            throw TelegramError(error)
        }
    }

}
