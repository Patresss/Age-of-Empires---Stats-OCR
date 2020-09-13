package com.patres.aoeocr.rename

import com.patres.aoeocr.filter.ImageFileFilter
import com.patres.aoeocr.path.ImageFileName
import com.patres.aoeocr.stats.model.Civilization
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class ImageFileRenamer(
    pathToDirectory: String,
    val civilization: Civilization
) {
    private val playerPathToDirectory = "$pathToDirectory\\$civilization"

    companion object {
        const val NUMBER_OF_IMAGES = 210
        val imageFileFilter = ImageFileFilter()
        val logger: Logger = LoggerFactory.getLogger(ImageFileRenamer::class.java)
    }

    @ExperimentalTime
    fun rename() {
        val time = measureTime {
            val listFiles = File(playerPathToDirectory)
                .listFiles(imageFileFilter)
                ?.toSortedSet(compareBy { it.lastModified() })
                ?: throw RuntimeException("Cannot create list of files with path $playerPathToDirectory")
            if (listFiles.size != NUMBER_OF_IMAGES) {
                throw RuntimeException("Invalid number of images. Should be $NUMBER_OF_IMAGES but is ${listFiles.size}")
            }

            val civImages = ImageFileName.createForCiv(civilization, true)
            if (civImages.size != NUMBER_OF_IMAGES) {
                throw RuntimeException("Invalid number of civsImages. Should be $NUMBER_OF_IMAGES but is ${civImages.size}")
            }

            listFiles.forEach { file ->
                val civImage = civImages[listFiles.indexOf(file)]
                file.renameTo(File(playerPathToDirectory, civImage.toString()))
            }
        }
        logger.info("Renamed all files - Time: ${time.inSeconds}s")
    }
}