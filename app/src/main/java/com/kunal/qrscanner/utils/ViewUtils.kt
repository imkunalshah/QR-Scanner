package com.kunal.qrscanner.utils

import android.content.Context
import android.view.View
import android.widget.Toast

/**
 * This Is An Extension
 * Function For Making
 * A View Visible
 **/
fun View.visible() {
    this.visibility = View.VISIBLE
}

/**
 * This Is An Extension
 * Function For Making
 * A View InVisible
 **/
fun View.inVisible() {
    this.visibility = View.INVISIBLE
}

/**
 * This Is An Extension
 * Function For Making
 * A View GONE
 **/
fun View.gone() {
    this.visibility = View.GONE
}

/**
 * This Is An Extension
 * Function For Showing
 * A Long Toast
 **/
fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

/**
 * This Is An Extension
 * Function For Showing
 * A Short Toast
 **/
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}