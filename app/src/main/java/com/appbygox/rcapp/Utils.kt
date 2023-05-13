package com.appbygox.rcapp

import java.text.SimpleDateFormat
import java.util.*

fun Int?.orZero(): Int {
    return this ?: 0
}

fun Long?.orZero(): Long {
    return this ?: 0
}

fun Long?.toFormatDate(): String {
    val timestamp: Long = this.orZero()
    val date = Date(timestamp)
    val format = SimpleDateFormat("HH:mm 'WIB' dd MMMM yyyy", Locale("id", "ID"))
    return format.format(date)
}