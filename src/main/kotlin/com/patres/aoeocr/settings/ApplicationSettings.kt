package com.patres.aoeocr.settings

import java.util.*

class ApplicationSettings(
    val pathToDirectory: String,
    val imageConverterSettings: ImageConverterSettings
) {

    companion object {
        fun loadFromResources(): ApplicationSettings {
            val propertiesFile = ApplicationSettings::class.java.getResourceAsStream("/settings.properties")
            val properties = Properties().apply {
                load(propertiesFile)
            }

            return ApplicationSettings(
                pathToDirectory = properties.getProperty("pathToDirectory"),
                imageConverterSettings = ImageConverterSettings(
                    saveImageFiles = properties.getProperty("saveImageFiles")?.toBoolean() ?: false,
                    replaceTheMostProbablyCharacters = properties.getProperty("replaceTheMostProbablyCharacters")?.toBoolean() ?: true,
                    useOnlyColoredImage = properties.getProperty("useOnlyColoredImage")?.toBoolean() ?: false
                )
            )
        }
    }

}