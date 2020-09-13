package com.patres.aoeocr.tesseract

import com.patres.aoeocr.utils.toIntStringOrFixMe
import com.patres.aoeocr.utils.toLocalTimeStringrFixMe
import net.sourceforge.tess4j.Tesseract

enum class TesseractType(private val whitelist: String) {

    DIGIT("1234567890 ") {
        override fun createTypedValue(value: String): String {
            return value.toIntStringOrFixMe()
        }
    },
    DIGIT_COLON("1234567890: ") {
        override fun createTypedValue(value: String): String {
            return value.toLocalTimeStringrFixMe()
        }
    },
    DIGIT_COLON_MINUS("1234567890:- ") {
        override fun createTypedValue(value: String): String {
            return value.toLocalTimeStringrFixMe()
        }
    };


    val tesseract: Tesseract = Tesseract().apply {
        setDatapath("tessdata")
        setTessVariable("user_defined_dpi", "96")
        setLanguage("eng")
        setTessVariable("tessedit_char_whitelist", whitelist)
    }

    open fun createTypedValue(value: String): String {
        return value
    }
}