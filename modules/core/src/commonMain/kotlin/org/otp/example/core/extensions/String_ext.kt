package org.otp.example.core.extensions

fun String.isDigitsOnly(): Boolean {
    return this.all { it.isDigit() }
}