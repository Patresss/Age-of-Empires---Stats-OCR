package com.patres.aoeocr

import com.patres.aoeocr.rename.ImageFileRenamer
import com.patres.aoeocr.settings.ApplicationSettings
import com.patres.aoeocr.stats.converter.ExcelStatsCreator
import com.patres.aoeocr.stats.model.Civilization
import com.patres.aoeocr.stats.model.StatsType
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {

    val applicationSettings = ApplicationSettings.loadFromResources()
    val imageConverterSettings = applicationSettings.imageConverterSettings

    // ================================
    // RENAME
    // ImageFileRenamer(applicationSettings.pathToDirectory, Civilization.MAGYARS).rename()

    // ================================
    // CREATE STATS

    // CIV_1 vs CIV_2 with type
    ExcelStatsCreator(applicationSettings).createStats(imageConverterSettings, Civilization.TEUTONS, Civilization.ITALIANS, StatsType.SOCIETY)

    // CIV_1 vs CIV_2
    // ExcelStatsCreator(applicationSettings).createStats(imageConverterSettings, Civilization.SPANISH, Civilization.FRANKS)

    // CIV_1 vs all
    // ExcelStatsCreator(applicationSettings).createStats(imageConverterSettings, Civilization.VIKINGS)
}


