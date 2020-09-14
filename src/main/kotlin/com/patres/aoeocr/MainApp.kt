package com.patres.aoeocr

import com.patres.aoeocr.settings.ApplicationSettings
import com.patres.aoeocr.stats.converter.ExcelStatsCreator
import com.patres.aoeocr.stats.model.Civilization
import com.patres.aoeocr.stats.model.StatsType
import org.slf4j.LoggerFactory
import kotlin.time.ExperimentalTime

private val logger = LoggerFactory.getLogger("MainApp")

@ExperimentalTime
fun main() {

    val applicationSettings = ApplicationSettings.loadFromResources()

    // ================================
    // RENAME
    // ImageFileRenamer(applicationSettings.pathToDirectory, Civilization.MAGYARS).rename()

    // ================================
    // CREATE STATS
    val excelStatsCreator = ExcelStatsCreator(applicationSettings)

    // CIV_1 vs CIV_2 with type
    val stats = excelStatsCreator.createStats(Civilization.TEUTONS, Civilization.ITALIANS, StatsType.SOCIETY)

    // CIV_1 vs CIV_2
    // val stats = excelStatsCreator.createStats(Civilization.SPANISH, Civilization.FRANKS)

    // CIV_1 vs all
    // val stats = excelStatsCreator.createStats(Civilization.VIKINGS)

    logger.info(
        """ 
Stats:
==========================
$stats
==========================
""".trimIndent()
    )

}




