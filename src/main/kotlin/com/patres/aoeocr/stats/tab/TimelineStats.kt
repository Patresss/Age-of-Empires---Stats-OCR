package com.patres.aoeocr.stats.tab

data class TimelineStats(
    val time: String
) : StatsTab() {

    override fun takeFieldsForExcel(): List<String> {
        return listOf(
            time
        )
    }
}