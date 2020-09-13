package com.patres.aoeocr.imageprocessor

import com.patres.aoeocr.utils.isTheSameColor
import java.awt.Color
import java.awt.Point
import java.awt.image.BufferedImage


object ImagePreparer {

    const val heightBinarizationLevel = 300
    const val mediumBinarizationLevel = 200
    const val lowBinarizationLevel = 115
    private val starColor = Color(226, 201, 69)
    private const val starEpsilon = 30
    private const val starSquareSize = 7

    fun doBinarization(image: BufferedImage, binarizationLevels: Int): BufferedImage {
        val newImage = copyImage(image)
        for (x in 0 until newImage.width) {
            for (y in 0 until newImage.height) {
                val rgb = Color(newImage.getRGB(x, y))
                val newColor = if (rgb.red + rgb.green + rgb.blue < binarizationLevels) {
                    Color.BLACK
                } else {
                    Color.WHITE
                }
                newImage.setRGB(x, y, newColor.rgb)
            }
        }
        return newImage
    }

    fun processImage(image: BufferedImage, binarizationLevels: Int): BufferedImage {
        val newImage = removeStar(copyImage(image))
        return doBinarization(newImage, binarizationLevels)
    }

    fun processImage(image: BufferedImage): BufferedImage {
        return removeStar(copyImage(image))
    }

    private fun removeStar(image: BufferedImage): BufferedImage {
        val starPoints = (0 until image.width).flatMap { x ->
            (0 until image.height)
                .flatMap { y -> calculateStarPoints(Color(image.getRGB(x, y)), x, y) }
        }.toSet()

        val backgroundColor = image.getRGB(0, 0)
        starPoints.forEach { point ->
            try {
                image.setRGB(point.x, point.y, backgroundColor)
            } catch (e: Exception) {
                // there is no pixel - ignore
            }
        }
        return image
    }

     fun copyImage(source: BufferedImage): BufferedImage {
        val b = BufferedImage(source.width, source.height, source.type)
        val g = b.graphics
        g.drawImage(source, 0, 0, null)
        g.dispose()
        return b
    }

    private fun calculateStarPoints(color: Color, x: Int, y: Int): Set<Point> {
        if (color.isTheSameColor(starColor, starEpsilon)) {
            return (-starSquareSize..starSquareSize).flatMap { xStar ->
                (-starSquareSize..starSquareSize).map { yStar -> Point(x + xStar, y + yStar) }
            }.toSet()
        }
        return emptySet()
    }

}