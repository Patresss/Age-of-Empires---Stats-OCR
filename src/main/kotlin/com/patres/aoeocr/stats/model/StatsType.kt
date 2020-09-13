package com.patres.aoeocr.stats.model

enum class StatsType {

    SCORE,
    MILITARY,
    ECONOMY,
    TECHNOLOGY,
    SOCIETY,
    TIMELINE;

    override fun toString() = name.toLowerCase().capitalize()

}