package com.patres.aoeocr.stats.model

data class GameStats(
    val playerStats1: PlayerStats,
    val playerStats2: PlayerStats
) {

    override fun toString() =
        """
            |GameStats(
            |    playerStats1=$playerStats1,
            |    playerStats2=$playerStats2
            |)
        """.trimMargin()

}