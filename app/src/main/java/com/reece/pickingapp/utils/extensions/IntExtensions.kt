package com.reece.pickingapp.utils.extensions

import java.time.LocalDateTime
import java.time.ZoneOffset

fun Int.asTimeIsValid(): Boolean {
    val curTime = (LocalDateTime.now(ZoneOffset.UTC)).atZone(ZoneOffset.UTC).toEpochSecond()

    return curTime < this.toLong()
}