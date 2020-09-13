package com.patres.aoeocr.stats.converter

import com.patres.aoeocr.settings.ImageConverterSettings
import com.patres.aoeocr.stats.model.Civilization
import com.patres.aoeocr.stats.model.Player
import com.patres.aoeocr.stats.tab.TechnologyStats
import com.patres.aoeocr.tesseract.TesseractType
import java.awt.image.BufferedImage

class ImageToTechnologyStatsConverter(
    imageFile: BufferedImage,
    imageConverterSettings: ImageConverterSettings
) : ImageToPlayersStatsConverter<TechnologyStats>(imageFile, imageConverterSettings) {

     fun calculateStats(player: Player, civilization: Civilization): TechnologyStats {
        val technologyStats = calculateStats(player)
        if (technologyStats.calculateNumberOfErrors(civilization) != 0) {
            val permutations = createPermutations(technologyStats)
            return permutations.minBy { it.calculateNumberOfErrors(civilization) }?: technologyStats
        }
        return technologyStats
    }

    private fun createPermutations(technologyStats: TechnologyStats): List<TechnologyStats> {
        return valueToList(technologyStats.researchCount).flatMap { researchCountElement ->
            valueToList(technologyStats.researchPercent).map { researchPercentElement ->
                TechnologyStats(
                    technologyStats.feudalAge,
                    technologyStats.castleAge,
                    technologyStats.imperialAge,
                    technologyStats.mapExplored,
                    researchCountElement,
                    researchPercentElement
                )
            }
        } + technologyStats
    }

    override fun calculateStats(player: Player): TechnologyStats {
        logger.debug("Creating stats for Technology")
        val mapExplored = takeWithEqualsSize(convertValue(player, 3, TesseractType.DIGIT), 2)
        return TechnologyStats(
            feudalAge = convertValue(player, 0, TesseractType.DIGIT_COLON_MINUS),
            castleAge = convertValue(player, 1, TesseractType.DIGIT_COLON_MINUS),
            imperialAge = convertValue(player, 2, TesseractType.DIGIT_COLON_MINUS),
            mapExplored = mapExplored,
            researchCount = convertValue(player, 4, TesseractType.DIGIT),
            researchPercent = convertValue(player, 5, TesseractType.DIGIT)
        )
    }

    override fun takeName() = "Technology"

}