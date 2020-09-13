package com.patres.aoeocr.stats.tab

import com.patres.aoeocr.stats.converter.ExcelStatsCreator.Companion.FIX_ME

abstract class StatsTab {

    companion object {
        const val EXCEL_FIX_ME = "$FIX_ME - calculate with excel"
    }

    abstract fun takeFieldsForExcel(): List<String>

    open fun calculateNumberOfErrors(): Int {
        return takeFieldsForExcel()
            .filterNot { it == EXCEL_FIX_ME  }
            .filter { it.contains(FIX_ME) }
            .count()
    }
}