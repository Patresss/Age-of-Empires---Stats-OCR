package com.patres.aoeocr.stats.tab

import com.patres.aoeocr.stats.model.Civilization
import kotlin.math.abs

data class SocietyStats(
    val totalWonders: String,
    val totalCastles: String,
    val relicsCaptured: String,
    val relicGold: String,
    val villagerHigh: String
) : StatsTab() {

    override fun takeFieldsForExcel(): List<String> {
        return listOf(
            totalWonders,
            totalCastles,
            relicsCaptured,
            relicGold,
            villagerHigh
        )
    }

    fun calculateNumberOfErrors(civilization: Civilization, societyScore: String): Int {
        val numberOfCastleIsValid = try {
            abs(civilization.castleCost * totalCastles.toInt() * 0.2 - societyScore.toDouble()) < 1
        } catch (e: Exception) {
            false
        }
        if (!numberOfCastleIsValid) {
            TechnologyStats.logger.warn("Number of castle is not valid")
        }
        return super.calculateNumberOfErrors() + if (numberOfCastleIsValid) 0 else 1
    }

}