package com.patres.aoeocr.imageprocessor

import java.awt.Color
import java.awt.image.BufferedImage
import java.lang.Integer.max

class ImageColumnsSplitter(private val imageFile: BufferedImage) {

    companion object {
        const val EPSILN = 10
        const val WIDTH = 100
    }

    fun split(): List<BufferedImage> {
        val lineToSplit = calculateLinesToSplit()
        return lineToSplit.map {
            val copyImage = ImagePreparer.copyImage(imageFile)
            val start = max(it - WIDTH / 2, 0)
            val width = if (start + WIDTH > copyImage.width) copyImage.width - start else WIDTH
            copyImage.getSubimage(start, 0, width, copyImage.height)
        }
    }

    fun calculateLinesToSplit(): List<Int> {
        val image = ImagePreparer.doBinarization(imageFile, 400)
        val line = (0 until image.width).map { x ->
            if ((0 until image.height).any { y -> image.getRGB(x, y) == Color.BLACK.rgb }) Color.BLACK else Color.WHITE
        }
        return calculateLinesToSplit(line)
    }

    private fun calculateLinesToSplit(line: List<Color>): List<Int> {
        val linesToSplit = mutableListOf<Int>()
        var firstBlackPoint: Int? = null
        var lastBlackPoint: Int? = null
        line.forEachIndexed { index, point ->
            if (firstBlackPoint == null && point == Color.BLACK) {
                firstBlackPoint = index
            }

            if (point == Color.BLACK) {
                lastBlackPoint = index
            }

            if (point == Color.WHITE && lastBlackPoint != null && firstBlackPoint != null && (index - lastBlackPoint!! > EPSILN || index == line.size - 1)) {
                linesToSplit.add(listOf(lastBlackPoint!!, firstBlackPoint!!).average().toInt())
                firstBlackPoint = null
                lastBlackPoint = null
            }
        }
        return linesToSplit
    }
}