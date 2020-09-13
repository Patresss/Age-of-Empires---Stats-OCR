package com.patres.aoeocr.stats.converter

import com.patres.aoeocr.settings.ImageConverterSettings
import com.patres.aoeocr.stats.model.Player
import com.patres.aoeocr.stats.tab.ScoreStats
import com.patres.aoeocr.tesseract.TesseractType
import java.awt.image.BufferedImage

class ImageToScoreStatsConverter(
    imageFile: BufferedImage,
    imageConverterSettings: ImageConverterSettings
) : ImageToPlayersStatsConverter<ScoreStats>(imageFile, imageConverterSettings) {

    override fun calculateStats(player: Player): ScoreStats {
        logger.debug("Creating stats for Score")
        val military = convertValue(player, 0, TesseractType.DIGIT)
        val economy = convertValue(player, 1, TesseractType.DIGIT)
        val technology = convertValue(player, 2, TesseractType.DIGIT)
        val society = convertValue(player, 3, TesseractType.DIGIT)
        val totalScore = convertValue(player, 4, TesseractType.DIGIT)

        val scoreStats = ScoreStats(
            military = military,
            economy = economy,
            technology = technology,
            society = society,
            totalScore = totalScore
        )

        if (scoreStats.calculateNumberOfErrors() != 0) {
            val permutations = createPermutations(military, economy, technology, society, totalScore, scoreStats)
            return permutations.firstOrNull { it.calculateNumberOfErrors() == 0}?: scoreStats
        }
        return scoreStats
    }

    private fun createPermutations(military: String, economy: String, technology: String, society: String, totalScore: String, scoreStats: ScoreStats): List<ScoreStats> {
        return valueToList(military).flatMap { militaryElement ->
            valueToList(economy).flatMap { economyElement ->
                valueToList(technology).flatMap { technologyElement ->
                    valueToList(society).flatMap { societyElement ->
                        valueToList(totalScore).map { totalScoreElement ->
                            ScoreStats(militaryElement, economyElement, technologyElement, societyElement, totalScoreElement)
                        }
                    }
                }
            }
        } + scoreStats
    }


    override fun takeName() = "Score"

}