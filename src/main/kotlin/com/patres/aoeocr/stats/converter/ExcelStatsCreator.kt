package com.patres.aoeocr.stats.converter

import com.patres.aoeocr.excel.ExcelConverter
import com.patres.aoeocr.path.GameImages
import com.patres.aoeocr.path.ImageFileName
import com.patres.aoeocr.settings.ApplicationSettings
import com.patres.aoeocr.settings.ImageConverterSettings
import com.patres.aoeocr.stats.StatsCreator
import com.patres.aoeocr.stats.model.Civilization
import com.patres.aoeocr.stats.model.Player
import com.patres.aoeocr.stats.model.StatsType
import com.patres.aoeocr.stats.tab.StatsTab.Companion.EXCEL_FIX_ME
import com.patres.aoeocr.validator.FileValidator
import net.sourceforge.tess4j.Tesseract
import org.slf4j.LoggerFactory
import java.io.File
import java.time.LocalTime
import javax.imageio.ImageIO
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

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
    fun createStats(imageConverterSettings: ImageConverterSettings, civilization: Civilization) {
        val time = measureTime {
            val playerPathToDirectory = "${applicationSettings.pathToDirectory}\\$civilization"
            val civilizationGames = ImageFileName.createForCiv(civilization)
            FileValidator().validate(civilizationGames, playerPathToDirectory)
            val excelStats = Civilization.values()
                .filter { it != civilization }
                .joinToString(System.lineSeparator()) { player2 ->
                    logger.info("Progress: |${"█".repeat(player2.ordinal + 1)}${" ".repeat(Civilization.values().size - player2.ordinal - 1)}|")
                    val gameImages = GameImages(civilization, player2)
                    val statsCreator = StatsCreator()
                    val stats = statsCreator.createStats(imageConverterSettings, gameImages, playerPathToDirectory)
                    excelConverter.toExcelFormat(stats)
                }
            val dirStats = File(applicationSettings.pathToDirectory, "Stats")
            if (!dirStats.exists()) {
                dirStats.mkdir()
            }
            File(dirStats, "$civilization - Stats.txt").writeText(excelStats)
            logger.info("Number of errors: ${takeNumberOfErrors(excelStats)}")
        }
        logger.info("Time: ${time.inSeconds}s")
    }


    @ExperimentalTime
    fun createStats(imageConverterSettings: ImageConverterSettings, civilization1: Civilization, civilization2: Civilization) {
        val time = measureTime {
            val playerPathToDirectory = "${applicationSettings.pathToDirectory}\\$civilization1"
            val gameImages = GameImages(civilization1, civilization2)
            val statsCreator = StatsCreator()
            val stats = statsCreator.createStats(imageConverterSettings, gameImages, playerPathToDirectory)
            val excelStats = excelConverter.toExcelFormat(stats)
            logger.info(
                """ 
Stats:
==========================
$excelStats
==========================
""".trimIndent()
            )
            logger.info("Number of errors: ${takeNumberOfErrors(excelStats)}")
        }
        logger.info("Time: ${time.inSeconds}s")
    }


    @ExperimentalTime
    fun createStats(imageConverterSettings: ImageConverterSettings, civilization1: Civilization, civilization2: Civilization, type: StatsType) {
        val time = measureTime {
            val playerPathToDirectory = "${applicationSettings.pathToDirectory}\\$civilization1"
            val imageFileName = ImageFileName(civilization1, civilization2, type)
            val processImage = ImageIO.read(imageFileName.toFile(playerPathToDirectory))
            val converter = when (type) {
                StatsType.SCORE -> ImageToScoreStatsConverter(processImage, imageConverterSettings)
                StatsType.MILITARY -> ImageToMilitaryStatsConverter(processImage, imageConverterSettings)
                StatsType.ECONOMY -> ImageToEconomyStatsConverter(processImage, LocalTime.of(1, 0, 0), imageConverterSettings)
                StatsType.TECHNOLOGY -> ImageToTechnologyStatsConverter(processImage, imageConverterSettings)
                StatsType.SOCIETY -> ImageToSocietyStatsConverter(processImage, imageConverterSettings)
                StatsType.TIMELINE -> ImageToTimelineStatsConverter(processImage, imageConverterSettings)
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
            logger.info(
                """ 
Stats:
==========================
$excelStats
==========================
""".trimIndent()
            )
            logger.info("Number of errors: ${takeNumberOfErrors(excelStats)}")
        }
        logger.info("Time: ${time.inSeconds}s")
    }

    private fun takeNumberOfErrors(excelStats: String): Int {
        return excelStats
            .replace(EXCEL_FIX_ME, "")
            .split(FIX_ME)
            .size - 1
    }
}