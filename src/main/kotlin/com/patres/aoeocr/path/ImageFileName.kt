package com.patres.aoeocr.path

import com.patres.aoeocr.stats.model.Civilization
import com.patres.aoeocr.stats.model.StatsType
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


class ImageFileName(
    private val civilization1: Civilization,
    private val civilization2: Civilization,
    val type: StatsType
) {

    companion object {
        fun createForCiv(civilization: Civilization, includeTheSame: Boolean = false): List<ImageFileName> {
            val allPlayers = Civilization.values()
                .map { Pair(civilization, it) }
                .filter { players -> players.first != players.second || includeTheSame }
            return allPlayers.flatMap { players ->
                StatsType.values().map { type ->
                    ImageFileName(
                        players.first,
                        players.second,
                        type
                    )
                }
            }
        }
    }

    fun toFile(pathToDirectory: String) = File(pathToDirectory, this.toString())

    fun loadImage(pathToDirectory: String): BufferedImage = ImageIO.read(toFile(pathToDirectory))

    override fun toString() = "$civilization1 vs $civilization2 - $type.jpg"

}