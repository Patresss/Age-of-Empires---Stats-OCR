package com.patres.aoeocr.stats.model

import com.patres.aoeocr.stats.tab.*

data class PlayerStats(
    val civilization: Civilization,
    val scoreStats: ScoreStats,
    val militaryStats: MilitaryStats,
    val economyStats: EconomyStats,
    val technologyStats: TechnologyStats,
    val societyStats: SocietyStats,
    val timelineStats: TimelineStats
) {

    override fun toString() =
        """|PlayerStats(
           |    player=$civilization,
           |    scoreStats=$scoreStats,
           |    militaryStats=$militaryStats,
           |    economyStats=$economyStats,
           |    technologyStats=$technologyStats,
           |    societyStats=$societyStats",
           |    timelineStats=$timelineStats"
           |)
        """.trimMargin()

    fun takeFieldsForExcel(): List<String> {
        return timelineStats.takeFieldsForExcel() +
                scoreStats.takeFieldsForExcel() +
                militaryStats.takeFieldsForExcel() +
                economyStats.takeFieldsForExcel() +
                technologyStats.takeFieldsForExcel() +
                societyStats.takeFieldsForExcel()
    }

}