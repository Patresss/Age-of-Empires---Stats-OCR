package com.patres.aoeocr.stats.converter

import com.patres.aoeocr.excel.ExcelConverter
import com.patres.aoeocr.path.GameImages
import com.patres.aoeocr.path.ImageFileName
import com.patres.aoeocr.settings.ApplicationSettings
import com.patres.aoeocr.stats.StatsCreator
import com.patres.aoeocr.stats.model.Civilization
import com.patres.aoeocr.stats.model.Player
import com.patres.aoeocr.stats.model.StatsType
import com.patres.aoeocr.stats.tab.StatsTab.Companion.EXCEL_FIX_ME
import com.patres.aoeocr.utils.executeAndMeasureTimeMillis
import com.patres.aoeocr.validator.FileValidator
import net.sourceforge.tess4j.Tesseract
import org.slf4j.LoggerFactory
import java.io.File
import java.time.LocalTime
import javax.imageio.ImageIO
import kotlin.time.ExperimentalTime

class ExcelStatsCreator(private val applicationSettings: ApplicationSettings) {

    companion object {
        private val logger = LoggerFactory.getLogger(ExcelStatsCreator::class.java)
        const val FIX_ME = "FIXME"
        val tesseract = Tesseract().apply {
            setDatapath("tessdata")
            setTessVariable("user_defined_dpi", "96")
            setTessVariable("tessedit_char_whitelist", "1234567890:/- ")
        }
        val excelConverter = ExcelConverter()
    }

    @ExperimentalTime
    fun createStats(civilization: Civilization): String {
        val (excelStats, time) = executeAndMeasureTimeMillis {
            val playerPathToDirectory = "${applicationSettings.pathToDirectory}\\$civilization"
            val civilizationGames = ImageFileName.createForCiv(civilization)
            FileValidator().validate(civilizationGames, playerPathToDirectory)
            val excelStats = Civilization.values()
                .filter { it != civilization }
                .joinToString(System.lineSeparator()) { player2 ->
                    logger.info("Progress: |${"█".repeat(player2.ordinal + 1)}${" ".repeat(Civilization.values().size - player2.ordinal - 1)}|")
                    val gameImages = GameImages(civilization, player2)
                    val statsCreator = StatsCreator()
                    val stats = statsCreator.createStats(applicationSettings.imageConverterSettings, gameImages, playerPathToDirectory)
                    excelConverter.toExcelFormat(stats)
                }
            val dirStats = File(applicationSettings.pathToDirectory, "Stats")
            if (!dirStats.exists()) {
                dirStats.mkdir()
            }
            File(dirStats, "$civilization - Stats.txt").writeText(excelStats)
            logger.info("Number of errors: ${takeNumberOfErrors(excelStats)}")
            excelStats
        }
        logger.info("Time: ${time.inSeconds}s")
        return excelStats
    }


    @ExperimentalTime
    fun createStats(civilization1: Civilization, civilization2: Civilization): String {
        val (excelStats, time) = executeAndMeasureTimeMillis {
            val playerPathToDirectory = "${applicationSettings.pathToDirectory}\\$civilization1"
            val gameImages = GameImages(civilization1, civilization2)
            val statsCreator = StatsCreator()
            val stats = statsCreator.createStats(applicationSettings.imageConverterSettings, gameImages, playerPathToDirectory)
            val excelStats = excelConverter.toExcelFormat(stats)
            logger.info("Number of errors: ${takeNumberOfErrors(excelStats)}")
            excelStats
        }
        logger.info("Time: ${time.inSeconds}s")
        return excelStats
    }


    @ExperimentalTime
    fun createStats(civilization1: Civilization, civilization2: Civilization, type: StatsType): String {
        val (excelStats, time) = executeAndMeasureTimeMillis {
            val playerPathToDirectory = "${applicationSettings.pathToDirectory}\\$civilization1"
            val imageFileName = ImageFileName(civilization1, civilization2, type)
            val processImage = ImageIO.read(imageFileName.toFile(playerPathToDirectory))
            val converter = when (type) {
                StatsType.SCORE -> ImageToScoreStatsConverter(processImage, applicationSettings.imageConverterSettings)
                StatsType.MILITARY -> ImageToMilitaryStatsConverter(processImage, applicationSettings.imageConverterSettings)
                StatsType.ECONOMY -> ImageToEconomyStatsConverter(processImage, LocalTime.of(1, 0, 0), applicationSettings.imageConverterSettings)
                StatsType.TECHNOLOGY -> ImageToTechnologyStatsConverter(processImage, applicationSettings.imageConverterSettings)
                StatsType.SOCIETY -> ImageToSocietyStatsConverter(processImage, applicationSettings.imageConverterSettings)
                StatsType.TIMELINE -> ImageToTimelineStatsConverter(processImage, applicationSettings.imageConverterSettings)
            }

            val excelStats = if (converter is ImageToTechnologyStatsConverter) {
                converter.calculateStats(Player.PLAYER1, civilization1).takeFieldsForExcel()
                    .joinToString("\t") + System.lineSeparator() + converter.calculateStats(Player.PLAYER2, civilization2).takeFieldsForExcel()
                    .joinToString("\t")
            } else {
                converter.calculateStats(Player.PLAYER1).takeFieldsForExcel()
                    .joinToString("\t") + System.lineSeparator() + converter.calculateStats(Player.PLAYER2).takeFieldsForExcel()
                    .joinToString("\t")
            }
            logger.info("Number of errors: ${takeNumberOfErrors(excelStats)}")
            excelStats
        }
        logger.info("Time: ${time.inSeconds}s")
        return excelStats
    }

    private fun takeNumberOfErrors(excelStats: String): Int {
        return excelStats
            .replace(EXCEL_FIX_ME, "")
            .split(FIX_ME)
            .size - 1
    }
}