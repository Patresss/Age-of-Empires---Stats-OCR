package com.patres.aoeocr.stats.converter

import com.patres.aoeocr.settings.ImageConverterSettings
import com.patres.aoeocr.stats.model.Player
import com.patres.aoeocr.stats.tab.MilitaryStats
import com.patres.aoeocr.tesseract.TesseractType
import java.awt.image.BufferedImage

class ImageToMilitaryStatsConverter(
    imageFile: BufferedImage,
    imageConverterSettings: ImageConverterSettings
) : ImageToPlayersStatsConverter<MilitaryStats>(imageFile, imageConverterSettings) {

    override fun calculateStats(player: Player): MilitaryStats {
        logger.debug("Creating stats for Military")
        val largestArmy = takeWithEqualsSize(convertValue(player, 5, TesseractType.DIGIT), 2)
        val calculatedStats = MilitaryStats(
            unitsKilled = convertValue(player, 0, TesseractType.DIGIT),
            unitsLost = convertValue(player, 1, TesseractType.DIGIT),
            buildingsRazed = convertValue(player, 2, TesseractType.DIGIT),
            buildingsLost = convertValue(player, 3, TesseractType.DIGIT),
            unitsConverted = convertValue(player, 4, TesseractType.DIGIT),
            largestArmy = largestArmy
        )
        if (calculatedStats.calculateNumberOfErrors() != 0) {
            val permutations = createPermutations(calculatedStats)
            return permutations.minBy { it.calculateNumberOfErrors() } ?: calculatedStats
        }
        return calculatedStats
    }

    private fun createPermutations(calculatedStats: MilitaryStats): List<MilitaryStats> {
        return valueToList(calculatedStats.largestArmy).map { largestArmyElement ->
            MilitaryStats(
                calculatedStats.unitsKilled,
                calculatedStats.unitsLost,
                calculatedStats.buildingsRazed,
                calculatedStats.buildingsLost,
                calculatedStats.unitsConverted,
                largestArmyElement
            )
        } + calculatedStats
    }

    override fun takeName() = "Military"

}