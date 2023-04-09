package com.github.otr.simple_client.helper

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.Hashtable

/**
 *
 */
internal object MyQrCodeTerminal {

    /**
     *
     */
    fun getQr(url: String?): String {
        val width = 40
        val height = 40
        val qrParam = Hashtable<EncodeHintType, Any?>()
        qrParam[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.L
        qrParam[EncodeHintType.CHARACTER_SET] = "utf-8"
        return try {
            val bitMatrix = MultiFormatWriter().encode(
                url, BarcodeFormat.QR_CODE, width, height, qrParam
            )
            toAscii(bitMatrix)
        } catch (ex: WriterException) {
            throw IllegalStateException("Can't encode QR code", ex)
        }
    }

    /**
     *
     */
    private fun toAscii(bitMatrix: BitMatrix): String {
        val sb = StringBuilder()
        for (rows in 0 until bitMatrix.height) {
            for (cols in 0 until bitMatrix.width) {
                val x = bitMatrix[rows, cols]
                sb.append(if (x) "  " else "██")
            }
            sb.append("\n")
        }
        return sb.toString()
    }

}
