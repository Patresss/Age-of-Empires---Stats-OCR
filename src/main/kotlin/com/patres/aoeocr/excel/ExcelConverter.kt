package com.patres.aoeocr.excel

import com.patres.aoeocr.stats.model.GameStats
import com.patres.aoeocr.stats.tab.StatsTab.Companion.EXCEL_FIX_ME

class ExcelConverter {

    companion object {
        const val SEPARATOR = "\t"
    }

    fun toExcelFormat(gameStats: GameStats): String {
        val player1Stats: List<String> =
            listOf(gameStats.playerStats1.civilization.toString()) +
            listOf(EXCEL_FIX_ME) +
            gameStats.playerStats1.takeFieldsForExcel()

        val player2Stats =
            listOf(gameStats.playerStats2.civilization.toString()) +
            listOf(EXCEL_FIX_ME) +
            gameStats.playerStats2.takeFieldsForExcel()

        return player1Stats.joinToString(SEPARATOR) + System.lineSeparator() +
               player2Stats.joinToString(SEPARATOR)
    }

}