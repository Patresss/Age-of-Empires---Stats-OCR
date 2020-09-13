package com.patres.aoeocr.filter

import java.io.File
import java.io.FileFilter

class ImageFileFilter : FileFilter {

    companion object {
        private val extensions = setOf("jpg", "png")
    }

    override fun accept(file: File): Boolean {
        return extensions.any { extension -> file.name.toLowerCase().endsWith(extension) }
    }
}