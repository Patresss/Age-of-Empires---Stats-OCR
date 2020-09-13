package com.patres.aoeocr.stats.converter

import com.patres.aoeocr.settings.ImageConverterSettings
import com.patres.aoeocr.stats.model.Civilization
import com.patres.aoeocr.stats.model.Player
import com.patres.aoeocr.stats.tab.SocietyStats
import com.patres.aoeocr.tesseract.TesseractType
import java.awt.image.BufferedImage

class ImageToSocietyStatsConverter(
    imageFile: BufferedImage,
    imageConverterSettings: ImageConverterSettings
) : ImageToPlayersStatsConverter<SocietyStats>(imageFile, imageConverterSettings) {

    fun calculateStats(player: Player, civilization: Civilization, societyScore: String): SocietyStats {
        val calculatedStats = calculateStats(player)
        if (calculatedStats.calculateNumberOfErrors(civilization, societyScore) != 0) {
            val permutations = createPermutations(calculatedStats)
            return permutations.minBy { it.calculateNumberOfErrors(civilization, societyScore) } ?: calculatedStats
        }
        return calculatedStats
    }

    private fun createPermutations(calculatedStats: SocietyStats): List<SocietyStats> {
        return valueToList(calculatedStats.totalCastles).map { totalCastlesElement ->
            SocietyStats(
                calculatedStats.totalWonders,
                totalCastlesElement,
                calculatedStats.relicsCaptured,
                calculatedStats.relicGold,
                calculatedStats.villagerHigh
            )
        } + calculatedStats
    }

    override fun calculateStats(player: Player): SocietyStats {
        logger.debug("Creating stats for Society")
        val totalWonders = take0IfOnly0or1(convertValue(player, 0, TesseractType.DIGIT))
        val relicsCaptured = take0IfOnly0or1(convertValue(player, 2, TesseractType.DIGIT))
        val relicGold = take0IfOnly0or1(convertValue(player, 3, TesseractType.DIGIT))
        return SocietyStats(
            totalWonders = totalWonders,
            totalCastles = convertValue(player, 1, TesseractType.DIGIT),
            relicsCaptured = relicsCaptured,
            relicGold = relicGold,
            villagerHigh = convertValue(player, 4, TesseractType.DIGIT)
        )
    }

    override fun takeName() = "Society"

}