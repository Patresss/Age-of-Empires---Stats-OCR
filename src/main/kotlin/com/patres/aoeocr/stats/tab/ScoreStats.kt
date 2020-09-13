package com.patres.aoeocr.stats.tab

import org.slf4j.Logger
import org.slf4j.LoggerFactory

data class ScoreStats(
    val military: String,
    val economy: String,
    val technology: String,
    val society: String,
    val totalScore: String
) : StatsTab() {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(StatsTab::class.java)
    }

    override fun takeFieldsForExcel(): List<String> {
        return listOf(
            military,
            economy,
            technology,
            society,
            totalScore
        )
    }

    override fun calculateNumberOfErrors(): Int {
        val scoreIsValid = try {
            military.toInt() + economy.toInt() + technology.toInt() + society.toInt() == totalScore.toInt()
        } catch (e: Exception) {
            false
        }
        if (!scoreIsValid) {
            logger.warn("Score is not valid")
        }
        return super.calculateNumberOfErrors() + if (scoreIsValid) 0 else 1
    }
}