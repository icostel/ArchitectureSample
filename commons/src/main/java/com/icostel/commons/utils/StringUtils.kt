package com.icostel.commons.utils

fun String.isDigitsOnly(): Boolean = try {
    this.toInt()
    true
} catch (e: NumberFormatException) {
    false
}

fun String.isNotDigitsOnly(): Boolean = try {
    this.toInt()
    false
} catch (e: NumberFormatException) {
    true
}
