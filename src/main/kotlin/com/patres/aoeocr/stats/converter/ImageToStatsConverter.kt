package com.patres.aoeocr.stats.converter

import com.patres.aoeocr.imageprocessor.ImageColumnsSplitter
import com.patres.aoeocr.stats.converter.ExcelStatsCreator.Companion.FIX_ME
import com.patres.aoeocr.stats.model.Player
import com.patres.aoeocr.stats.tab.StatsTab
import com.patres.aoeocr.imageprocessor.ImagePreparer
import com.patres.aoeocr.settings.ImageConverterSettings
import com.patres.aoeocr.tesseract.OcrCharactersConverter
import com.patres.aoeocr.tesseract.OcrReader
import com.patres.aoeocr.tesseract.TesseractType
import com.patres.aoeocr.utils.toIntStringOrFixMe
import com.patres.aoeocr.utils.toLocalTimeStringrFixMe
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.awt.image.BufferedImage

abstract class ImageToStatsConverter<T : StatsTab>(
    player1ImageFile: BufferedImage,
    player2ImageFile: BufferedImage,
    val imageConverterSettings: ImageConverterSettings
) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(ImageToStatsConverter::class.java)
        val ocrCharactersConverter = OcrCharactersConverter(mapOf(
            setOf('0', '6') to  '6',
            setOf('0', '1') to  '0',
            setOf('0', '9') to  '9',
            setOf('1', '7') to  '7',
            setOf('2', '5') to  '5',
            setOf('3', '4') to  '4',
            setOf('3', '5') to  '5',
            setOf('3', '8') to  '8',
            setOf('4', '9') to  '9'
        ))
    }

    private val statsPlayer1Images = ImageColumnsSplitter(player1ImageFile).split()
    private val statsPlayer2Images = ImageColumnsSplitter(player2ImageFile).split()

    fun convertValue(player: Player, index: Int, tesseractType: TesseractType): String {
        val images = when (player) {
            Player.PLAYER1 -> statsPlayer1Images
            Player.PLAYER2 -> statsPlayer2Images
        }

        val imageWithValue = images[index]
        val ocrReader = OcrReader(tesseractType, imageWithValue, "${takeName()} $player $index", imageConverterSettings.saveImageFiles)

        val valueFromColoredImage = ocrReader.doOcr(null)
        if (imageConverterSettings.useOnlyColoredImage) {
            return valueFromColoredImage
        }
        val valueFromLowBinarizationImage = ocrReader.doOcr(ImagePreparer.lowBinarizationLevel)
        val valueFromMediumBinarizationImage = ocrReader.doOcr(ImagePreparer.mediumBinarizationLevel)
        val valueFromHeightBinarizationImage = ocrReader.doOcr(ImagePreparer.heightBinarizationLevel)

        val convertedValues = setOf(valueFromColoredImage, valueFromLowBinarizationImage, valueFromMediumBinarizationImage, valueFromHeightBinarizationImage)
            .filterNot { it.contains(FIX_ME) }
            .toSet()

        if (convertedValues.size > 1) {
            logger.warn("Cannot convert value. Possible cases: $convertedValues")
            val allValues = "$FIX_ME $convertedValues"
            if (imageConverterSettings.replaceTheMostProbablyCharacters) {
                return ocrCharactersConverter.convertValues(convertedValues)?: allValues
            }
            return allValues
        }
        return convertedValues.firstOrNull() ?: valueFromColoredImage
    }

    fun takeIntElement(statsList: List<String>, index: Int): String {
        return try {
            statsList[index].toIntStringOrFixMe()
        } catch (e: Exception) {
            logger.error("Cannot parse value $statsList, ${e.message}")
            FIX_ME
        }
    }

    fun takeLocalTimeElement(statsList: List<String>, index: Int): String? {
        return try {
            statsList[index].toLocalTimeStringrFixMe()
        } catch (e: Exception) {
            logger.error("Cannot parse value $statsList, ${e.message}")
            FIX_ME
        }
    }

    fun takeValidField(value1: String?, value2: String?) = if (value1?.contains(FIX_ME) == true) value2 else value1
    fun takeValidFieldNotNull(value1: String, value2: String) = if (value1.contains(FIX_ME)) value2 else value1

    fun valueToList(value: String): List<String> {
        return if (value.contains(FIX_ME)) {
            value
                .replace(FIX_ME, "")
                .replace("\\s".toRegex(), "")
                .replace("[", "")
                .replace("]", "")
                .split(",")
        } else {
            listOf(value)
        }
    }

    fun take0IfOnly0or1(convertedValue: String): String {
        val convertedValueList = valueToList(convertedValue)
        return if (convertedValueList.size > 1) {
            val filteredValuesAttribute = convertedValueList
                .filter { convertedValueList.size == 2  && convertedValueList.containsAll(listOf("0", "1")) && it == "0" }
            if (filteredValuesAttribute.size == 1) {
                return filteredValuesAttribute[0]
            }
            return convertedValue
        } else {
            convertedValue
        }
    }

    fun takeWithEqualsSize(convertedValue: String, length: Int): String {
        return takeWithPredicate(convertedValue) { it.length == length }

    }

    fun takeWithLessOrEqualsSize(convertedValue: String, length: Int): String {
        return takeWithPredicate(convertedValue) { it.length <= length }
    }

    abstract fun calculateStats(player: Player): T
    abstract fun takeName(): String

    private fun takeWithPredicate(convertedValue: String, predicate: (String) -> Boolean): String {
        val convertedValueList = valueToList(convertedValue)
        return if (convertedValueList.size > 1) {
            val filteredValuesAttribute = convertedValueList.filter { predicate.invoke(it) }
            if (filteredValuesAttribute.size == 1) {
                return filteredValuesAttribute[0]
            }
            return convertedValue
        } else {
            convertedValue
        }
    }

}