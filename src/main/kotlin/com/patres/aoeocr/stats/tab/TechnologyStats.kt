package com.patres.aoeocr.stats.tab

import com.patres.aoeocr.stats.model.Civilization
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.math.abs

data class TechnologyStats(
    val feudalAge: String?,
    val castleAge: String?,
    val imperialAge: String?,
    val mapExplored: String,
    val researchCount: String,
    val researchPercent: String
) : StatsTab() {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(TechnologyStats::class.java)
    }

    override fun takeFieldsForExcel(): List<String> {
        return listOf(
            feudalAge?: "",
            castleAge?: "",
            imperialAge?: "",
            mapExplored,
            researchCount,
            researchPercent
        )
    }

    fun calculateNumberOfErrors(civilization: Civilization): Int {
        val researchIsValid = try {
            val validResearchPercent = researchCount.toDouble() / civilization.researchNumber * 100
            abs(validResearchPercent - researchPercent.toInt()) <= 2
        } catch (e: Exception) {
            false
        }
        if (!researchIsValid) {
            logger.warn("Research is not valid, should be ${civilization.researchNumber}")
        }
        return super.calculateNumberOfErrors() + if (researchIsValid) 0 else 1
    }

}