package com.patres.aoeocr.tesseract

import com.patres.aoeocr.imageprocessor.ImagePreparer
import org.slf4j.LoggerFactory
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class OcrReader(
    private val tesseractType: TesseractType,
    private val inputFile: BufferedImage,
    private val name: String = "",
    private val saveImageFiles: Boolean = false
) {

    companion object {
        private val logger = LoggerFactory.getLogger(OcrReader::class.java)
    }

    fun doOcr(binarizationLevels: Int?): String {
        val preparedFile = if (binarizationLevels == null) ImagePreparer.processImage(inputFile) else ImagePreparer.processImage(inputFile, binarizationLevels)
        if (saveImageFiles) {
            ImageIO.write(preparedFile, "png", File("test $name - $binarizationLevels.png"))
        }
        val text = tesseractType.tesseract.doOCR(preparedFile).replace("\\s".toRegex(), "")
        logger.debug("OCR = Read text binary - $binarizationLevels: $text")
        return tesseractType.createTypedValue(text)
    }

}
