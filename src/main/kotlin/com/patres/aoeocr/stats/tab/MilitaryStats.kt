package com.patres.aoeocr.stats.tab

data class MilitaryStats(
    val unitsKilled: String,
    val unitsLost: String,
    val buildingsRazed: String,
    val buildingsLost: String,
    val unitsConverted: String,
    val largestArmy: String
) : StatsTab() {

    override fun takeFieldsForExcel(): List<String> {
        return listOf(
            unitsKilled,
            unitsLost,
            buildingsRazed,
            buildingsLost,
            unitsConverted,
            largestArmy
        )
    }

    override fun calculateNumberOfErrors(): Int {
        val scoreIsValid = try {
            largestArmy.length == 2
        } catch (e: Exception) {
            false
        }
        if (!scoreIsValid) {
            ScoreStats.logger.warn("Largest army is not valid")
        }
        return super.calculateNumberOfErrors() + if (scoreIsValid) 0 else 1
    }
}