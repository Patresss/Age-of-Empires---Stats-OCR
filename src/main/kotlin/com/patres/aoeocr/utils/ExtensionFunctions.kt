package com.patres.aoeocr.utils

import com.patres.aoeocr.stats.converter.ExcelStatsCreator.Companion.FIX_ME
import org.slf4j.LoggerFactory
import java.awt.Color
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

val logger = LoggerFactory.getLogger("ExtensionFunctions.kt")

fun String.toLocalTimeStringrFixMe(): String {
    return try {
        return this.toLocalTime()?.format(DateTimeFormatter.ISO_TIME)?: ""
    } catch (e: Exception) {
        logger.error("Cannot parse value $this to LocalTime")
        "$FIX_ME - $this"
    }
}

fun String.toLocalTime(): LocalTime? {
    if (this == "-" || this == "") {
        return null
    }
    val splitTime = this.split(":")

    if (splitTime.size != 3) {
        throw RuntimeException("Invalid number of element for local time: $this")
    }
    val hour = splitTime[0]
    val fixedHour = if (hour.startsWith("1")) hour.replaceFirst("1", "0") else hour
    return LocalTime.of(fixedHour.toInt(), splitTime[1].toInt(), splitTime[2].toInt())
}

fun String.toIntStringOrFixMe(): String {
    return try {
        if (this.startsWith("0") && this.length > 1) "$FIX_ME - $this" else  this.toInt().toString()
    } catch (e: Exception) {
        logger.error("Cannot parse value $this to Int")
        "$FIX_ME - $this"
    }
}


fun Color.isTheSameColor(other: Color, epsilon: Int): Boolean {
    return abs(this.red - other.red) < epsilon && abs(this.green - other.green) < epsilon && abs(this.blue - other.blue) < epsilon
}

