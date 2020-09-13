package com.patres.aoeocr.validator

import com.patres.aoeocr.path.ImageFileName

class FileValidator{

    fun validate(civilizationGames: List<ImageFileName>, pathToDirectory: String) {
        val notFoundFiles = civilizationGames
            .map { it.toFile(pathToDirectory) }
            .filterNot { it.exists() }
            .map { it.toString() }

        if (notFoundFiles.isNotEmpty()) {
            throw RuntimeException("Files not found: $notFoundFiles")
        }
    }
}