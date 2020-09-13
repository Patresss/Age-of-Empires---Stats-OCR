package com.patres.aoeocr.stats.converter

import com.patres.aoeocr.settings.ImageConverterSettings
import com.patres.aoeocr.stats.model.Player
import com.patres.aoeocr.stats.tab.TimelineStats
import com.patres.aoeocr.tesseract.TesseractType
import java.awt.image.BufferedImage

class ImageToTimelineStatsConverter(
    imageFile: BufferedImage,
    imageConverterSettings: ImageConverterSettings
) : ImageToStatsConverter<TimelineStats>(
    imageFile.getSubimage(1485, 185, 100, 30),
    imageFile.getSubimage(1485, 185, 100, 30),
    imageConverterSettings) {

    override fun calculateStats(player: Player): TimelineStats {
        logger.debug("Creating stats for Timeline")
        val time = convertValue(player, 0, TesseractType.DIGIT_COLON)
        return TimelineStats(
            time = time
        )
    }

    override fun takeName() = "Timeline"

}