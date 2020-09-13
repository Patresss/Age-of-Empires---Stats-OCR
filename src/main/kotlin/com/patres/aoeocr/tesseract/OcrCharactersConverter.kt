package com.patres.aoeocr.tesseract

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class OcrCharactersConverter(private val characterToReplaceMap: Map<Set<Char>, Char>) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(OcrCharactersConverter::class.java)
    }

    fun convertValues(convertedValues: Set<String>): String? {
        if (is0Value(convertedValues)) {
            return "0"
        }

        if (convertedValues.size != 2 || convertedValues.map { it.length }.distinct().size != 1) {
            return null
        }

        val first = convertedValues.first()
        val second = convertedValues.last()
        val differentIndexCharacters = differentIndexCharacters(first, second)

        if (differentIndexCharacters.size != 1) {
            return null
        }
        val differentIndexCharacter = differentIndexCharacters.first()

        val validCharacter = characterToReplaceMap[setOf(first[differentIndexCharacter], second[differentIndexCharacter])]
        return when (validCharacter) {
            first[differentIndexCharacter] -> first
            second[differentIndexCharacter] -> second
            else -> {
                logger.error("Cannot find valid character. First: $first, second: $second")
                null
            }
        }
    }

    private fun is0Value(convertedValues: Set<String>): Boolean {
        return convertedValues.size == 2 && convertedValues.containsAll(setOf("0", "11")) ||
                convertedValues.size == 3 && convertedValues.containsAll(setOf("0", "1", "11"))
    }

    private fun differentIndexCharacters(first: String, second: String): List<Int> {
        if (first.length != second.length) {
            throw RuntimeException("Length of strings are not the same")
        }
        return (first.indices).mapNotNull { index ->
            if (first[index] != second[index]) index else null
        }
    }
}

