package com.patres.aoeocr.stats.model

enum class Civilization(val researchNumber: Int, val castleCost: Double = 650.0) {

    AZTECS(80),
    BERBERS(92),
    BRITONS(90),
    BULGARIANS(82),
    BURMESE(89),
    BYZANTINES(93),
    CELTS(85),
    CHINESE(92),
    CUMANS(81),
    ETHIOPIANS(92),
    FRANKS(85,650.0 * 0.75),
    GOTHS(80),
    HUNS(76),
    INCAS(87,650.0 * 0.85),
    INDIANS(88),
    ITALIANS(92),
    JAPANESE(89),
    KHMER(90),
    KOREANS(86),
    LITHUANIANS(91),
    MAGYARS(89),
    MALAY(87),
    MALIANS(89),
    MAYANS(85),
    MONGOLS(85),
    PERSIANS(88),
    PORTUGUESE(91),
    SARACENS(94),
    SLAVS(88),
    SPANISH(94),
    TATARS(85),
    TEUTONS(90),
    TURKS(91),
    VIETNAMESE(91),
    VIKINGS(85);

    override fun toString() = name.toLowerCase().capitalize()
}