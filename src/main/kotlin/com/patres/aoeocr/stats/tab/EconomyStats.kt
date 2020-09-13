package com.patres.aoeocr.stats.tab

import java.time.LocalTime

data class EconomyStats(
    val foodCollected: String,
    val woodCollected: String,
    val stoneCollected: String,
    val goldCollected: String,
    val time: LocalTime
) : StatsTab() {

    override fun takeFieldsForExcel(): List<String> {
        return listOf(
            foodCollected,
            EXCEL_FIX_ME,
            woodCollected,
            EXCEL_FIX_ME,
            stoneCollected,
            EXCEL_FIX_ME,
            goldCollected,
            EXCEL_FIX_ME
        )
    }

}