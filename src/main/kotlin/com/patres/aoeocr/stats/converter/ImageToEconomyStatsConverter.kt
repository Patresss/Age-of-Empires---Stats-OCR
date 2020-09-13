package com.patres.aoeocr.stats.converter

import com.patres.aoeocr.settings.ImageConverterSettings
import com.patres.aoeocr.stats.model.Player
import com.patres.aoeocr.stats.tab.EconomyStats
import com.patres.aoeocr.tesseract.TesseractType
import java.awt.image.BufferedImage
import java.time.LocalTime

class ImageToEconomyStatsConverter(
    imageFile: BufferedImage,
    val time: LocalTime,
    imageConverterSettings: ImageConverterSettings
) : ImageToPlayersStatsConverter<EconomyStats>(imageFile, imageConverterSettings) {

    override fun calculateStats(player: Player): EconomyStats {
        logger.debug("Creating stats for Economy")
        val stoneCollected = takeWithLessOrEqualsSize(convertValue(player, 2, TesseractType.DIGIT), 4)
        val goldCollected = takeWithLessOrEqualsSize(convertValue(player, 3, TesseractType.DIGIT), 5)
        return EconomyStats(
            foodCollected = convertValue(player, 0, TesseractType.DIGIT),
            woodCollected = convertValue(player, 1, TesseractType.DIGIT),
            stoneCollected = stoneCollected,
            goldCollected = goldCollected,
            time = time
        )
    }

    override fun takeName() = "Economy"

}