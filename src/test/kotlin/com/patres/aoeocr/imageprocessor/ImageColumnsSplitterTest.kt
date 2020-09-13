package com.patres.aoeocr.imageprocessor

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File
import javax.imageio.ImageIO


internal class ImageColumnsSplitterTest( ) {

    @Test
    fun shouldReturnSplitLines() {
        // given
        val image = ImageIO.read(ImageColumnsSplitterTest::class.java.getResource("/image/test player1 - null.png"))
        val imageColumnsSplitter = ImageColumnsSplitter(image)

        // when
        val linesToSplit = imageColumnsSplitter.calculateLinesToSplit()

        // then
        assertEquals(listOf(162, 284, 376, 484, 606, 699, 807), linesToSplit)
    }

    @Test
    fun shouldReturnSplitImages() {
        // given
        val image = ImageIO.read(ImageColumnsSplitterTest::class.java.getResource("/image/test player1 - null.png"))
        val imageColumnsSplitter = ImageColumnsSplitter(image)

        // when
        val images = imageColumnsSplitter.split()

        // then
        assertEquals(7, images.size)

        images.forEach { ImageIO.write(it, "png", File("P:\\Programowanie\\Projekty\\Age of Empires - Stats OCR\\test ${images.indexOf(it)}.png"))}
    }

}