package com.kunal.qrscanner.utils

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * This Is A Util Function
 * For Creating A
 * QR Code from given data
 **/
fun getQrCodeBitmap(data: String): Bitmap {
    val size = 512 //pixels
    val hints = hashMapOf<EncodeHintType, Int>().also {
        it[EncodeHintType.MARGIN] = 1
    } // Make the QR code buffer border narrower
    val bits = QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size)
    return Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
        for (x in 0 until size) {
            for (y in 0 until size) {
                it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
            }
        }
    }
}

/**
 * This Is A Util Function
 * For Getting Formatted Date
 * From Millis
 **/
fun getFormattedDate(millis: Long): String {
    val date = Date(millis)
    val formatter = SimpleDateFormat("dd MMM yyyy")
    return formatter.format(date)
}

/**
 * This Is An Extension Function
 * For Converting Dp
 * Into Pixels (Used For Giving Padding Programmatically)
 **/
fun Context.pxFromDp(sizeInDp: Int): Int {
    val scale: Float = this.resources.displayMetrics.density
    return (sizeInDp * scale + 0.5f).toInt()
}

/**
 * This Is An Extension Function
 * For Giving A Haptic Feedback
 * To The User On Events, clicks etc
 **/
fun Context.giveHapticFeedback() {
    val vibrator = this.getSystemService(VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(80L, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(80L)
    }
}