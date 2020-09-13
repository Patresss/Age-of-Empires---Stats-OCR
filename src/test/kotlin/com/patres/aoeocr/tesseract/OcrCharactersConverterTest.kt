package com.patres.aoeocr.tesseract

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class OcrCharactersConverterTest {

    companion object {
        val characterToReplaceMap = mapOf(
            setOf('0', '6') to  '6'
        )
    }

    @Test
    fun shouldReplaceCharactersWhenLengthIs1() {
        // given
        val ocrCharactersConverter = OcrCharactersConverter(characterToReplaceMap)
        val convertedValues = setOf("0", "6")

        // when
        val convertValues = ocrCharactersConverter.convertValues(convertedValues)

        // then
        assertEquals("6", convertValues)
    }

    @Test
    fun shouldReplaceCharactersWhenLengthMoreThan1() {
        // given
        val ocrCharactersConverter = OcrCharactersConverter(characterToReplaceMap)
        val convertedValues = setOf("666123", "606123")

        // when
        val convertValues = ocrCharactersConverter.convertValues(convertedValues)

        // then
        assertEquals("666123", convertValues)
    }

    @Test
    fun shouldNotReplaceCharactersWhenMoreThanOneDifferent() {
        // given
        val ocrCharactersConverter = OcrCharactersConverter(characterToReplaceMap)
        val convertedValues = setOf("60606", "60066")

        // when
        val convertValues = ocrCharactersConverter.convertValues(convertedValues)

        // then
        assertNull(convertValues)
    }

    @Test
    fun shouldNotReplaceCharactersWhenMoreThanTwoElements() {
        // given
        val ocrCharactersConverter = OcrCharactersConverter(characterToReplaceMap)
        val convertedValues = setOf("60616", "60166", "60266")

        // when
        val convertValues = ocrCharactersConverter.convertValues(convertedValues)

        // then
        assertNull(convertValues)
    }


}