package com.patres.aoeocr.stats

import com.patres.aoeocr.path.GameImages
import com.patres.aoeocr.settings.ImageConverterSettings
import com.patres.aoeocr.stats.converter.*
import com.patres.aoeocr.stats.model.GameStats
import com.patres.aoeocr.stats.model.Player
import com.patres.aoeocr.stats.model.PlayerStats
import com.patres.aoeocr.utils.toLocalTime
import org.slf4j.LoggerFactory

class StatsCreator {

    companion object {
        private val logger = LoggerFactory.getLogger(StatsCreator::class.java)
    }

    fun createStats(imageConverterSettings: ImageConverterSettings, gameImages: GameImages, path: String): GameStats {
        logger.debug("******** Creating stats for: ${gameImages.civilization1} and ${gameImages.civilization2} ********")

        val imageToTimelineStatsConverter = ImageToTimelineStatsConverter(gameImages.timelineImageFileName.loadImage(path), imageConverterSettings)
        val timelineStats = imageToTimelineStatsConverter.calculateStats(Player.PLAYER1)
        val imageToScoreStatsConverter = ImageToScoreStatsConverter(gameImages.scoreImageFileName.loadImage(path), imageConverterSettings)
        val imageToMilitaryStatsConverter = ImageToMilitaryStatsConverter(gameImages.militaryImageFileName.loadImage(path), imageConverterSettings)
        val imageToEconomyStatsConverter = ImageToEconomyStatsConverter( gameImages.economyImageFileName.loadImage(path), timelineStats.time.toLocalTime() ?: throw RuntimeException("Cannot create LocalTime"), imageConverterSettings)
        val imageToTechnologyStatsConverter = ImageToTechnologyStatsConverter(gameImages.technologyImageFileName.loadImage(path), imageConverterSettings)
        val imageToSocietyStatsConverter = ImageToSocietyStatsConverter(gameImages.societyImageFileName.loadImage(path), imageConverterSettings)

        val scoreStats1 = imageToScoreStatsConverter.calculateStats(Player.PLAYER1)
        val scoreStats2 = imageToScoreStatsConverter.calculateStats(Player.PLAYER2)
        return GameStats(
            playerStats1 = PlayerStats(
                civilization = gameImages.civilization1,
                scoreStats = scoreStats1,
                militaryStats = imageToMilitaryStatsConverter.calculateStats(Player.PLAYER1),
                economyStats = imageToEconomyStatsConverter.calculateStats(Player.PLAYER1),
                technologyStats = imageToTechnologyStatsConverter.calculateStats(Player.PLAYER1, gameImages.civilization1),
                societyStats = imageToSocietyStatsConverter.calculateStats(Player.PLAYER1, gameImages.civilization1, scoreStats1.society),
                timelineStats = timelineStats
            ),
            playerStats2 = PlayerStats(
                civilization = gameImages.civilization2,
                scoreStats = scoreStats2,
                militaryStats = imageToMilitaryStatsConverter.calculateStats(Player.PLAYER2),
                economyStats = imageToEconomyStatsConverter.calculateStats(Player.PLAYER2),
                technologyStats = imageToTechnologyStatsConverter.calculateStats(Player.PLAYER2, gameImages.civilization2),
                societyStats = imageToSocietyStatsConverter.calculateStats(Player.PLAYER2, gameImages.civilization2, scoreStats2.society),
                timelineStats = timelineStats
            )
        )
    }

}