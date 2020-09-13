package com.patres.aoeocr.path

import com.patres.aoeocr.stats.model.Civilization
import com.patres.aoeocr.stats.model.StatsType


class GameImages(
    val civilization1: Civilization,
    val civilization2: Civilization
) {

    val scoreImageFileName = ImageFileName(civilization1, civilization2, StatsType.SCORE)
    val militaryImageFileName = ImageFileName(civilization1, civilization2, StatsType.MILITARY)
    val economyImageFileName = ImageFileName(civilization1, civilization2, StatsType.ECONOMY)
    val technologyImageFileName = ImageFileName(civilization1, civilization2, StatsType.TECHNOLOGY)
    val societyImageFileName = ImageFileName(civilization1, civilization2, StatsType.SOCIETY)
    val timelineImageFileName = ImageFileName(civilization1, civilization2, StatsType.TIMELINE)

}