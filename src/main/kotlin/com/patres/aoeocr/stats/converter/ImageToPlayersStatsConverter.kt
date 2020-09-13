package com.patres.aoeocr.stats.converter

import com.patres.aoeocr.settings.ImageConverterSettings
import com.patres.aoeocr.stats.tab.StatsTab
import java.awt.image.BufferedImage

abstract class ImageToPlayersStatsConverter<T : StatsTab>(
    imageFile: BufferedImage,
    imageConverterSettings: ImageConverterSettings
) : ImageToStatsConverter<T>(
    imageFile.getSubimage(730, 300, 860, 20),
    imageFile.getSubimage(730, 350, 860, 20),
    imageConverterSettings
)