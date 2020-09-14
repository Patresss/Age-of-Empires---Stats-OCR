package com.patres.aoeocr.stats.converter

import com.patres.aoeocr.settings.ApplicationSettings
import com.patres.aoeocr.settings.ImageConverterSettings
import com.patres.aoeocr.stats.model.Civilization
import com.patres.aoeocr.stats.model.StatsType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URLDecoder
import kotlin.time.ExperimentalTime

@ExperimentalTime
internal class ExcelStatsCreatorTest {

    @Test
    fun `should convert values when only colored and do not replace the most probably characters`() {
        // given
        val applicationSettings = ApplicationSettings(
            pathToDirectory = URLDecoder.decode(ExcelStatsCreatorTest::class.java.getResource("/stats").file, "UTF-8"),
            imageConverterSettings = ImageConverterSettings(
                saveImageFiles = false,
                replaceTheMostProbablyCharacters = false,
                useOnlyColoredImage = true
            )
        )
        val excelStatsCreator = ExcelStatsCreator(applicationSettings)

        // when
        val convertValues = excelStatsCreator.createStats(Civilization.VIKINGS, Civilization.CELTS, StatsType.MILITARY)

        // then
        val expected = "" +
                "394	288	88	0	1	71" + System.lineSeparator() +
                "283	395	0	88	5	72"
        assertEquals(expected, convertValues)
    }


    @Test
    fun `should convert values when not only colored and do not replace the most probably characters`() {
        // given
        val applicationSettings = ApplicationSettings(
            pathToDirectory = URLDecoder.decode(ExcelStatsCreatorTest::class.java.getResource("/stats").file, "UTF-8"),
            imageConverterSettings = ImageConverterSettings(
                saveImageFiles = false,
                replaceTheMostProbablyCharacters = false,
                useOnlyColoredImage = false
            )
        )
        val excelStatsCreator = ExcelStatsCreator(applicationSettings)

        // when
        val convertValues = excelStatsCreator.createStats(Civilization.VIKINGS, Civilization.CELTS, StatsType.MILITARY)

        // then
        val expected = "" +
                "394	288	88	FIXME [0, 1]	1	71" + System.lineSeparator() +
                "283	395	0	FIXME [88, 85]	5	72"
        assertEquals(expected, convertValues)
    }

    @Test
    fun `should convert values when not only colored and replace the most probably characters`() {
        // given
        val applicationSettings = ApplicationSettings(
            pathToDirectory = URLDecoder.decode(ExcelStatsCreatorTest::class.java.getResource("/stats").file, "UTF-8"),
            imageConverterSettings = ImageConverterSettings(
                saveImageFiles = false,
                replaceTheMostProbablyCharacters = true,
                useOnlyColoredImage = false
            )
        )
        val excelStatsCreator = ExcelStatsCreator(applicationSettings)

        // when
        val convertValues = excelStatsCreator.createStats(Civilization.VIKINGS, Civilization.CELTS, StatsType.MILITARY)

        // then
        val expected = "" +
                "394	288	88	0	1	71" + System.lineSeparator() +
                "283	395	0	FIXME [88, 85]	5	72"
        assertEquals(expected, convertValues)
    }

    @Test
    fun `should convert values stats when not only colored and replace the most probably characters`() {
        // given
        val applicationSettings = ApplicationSettings(
            pathToDirectory = URLDecoder.decode(ExcelStatsCreatorTest::class.java.getResource("/stats").file, "UTF-8"),
            imageConverterSettings = ImageConverterSettings(
                saveImageFiles = false,
                replaceTheMostProbablyCharacters = true,
                useOnlyColoredImage = false
            )
        )
        val excelStatsCreator = ExcelStatsCreator(applicationSettings)

        // when
        val convertValues = excelStatsCreator.createStats(Civilization.VIKINGS, Civilization.CELTS)

        // then
        val expected = "" +
                "Vikings	FIXME - calculate with excel	00:55:12	7481	6153	5127	390	19151	394	288	88	0	1	71	32999	FIXME - calculate with excel	32593	FIXME - calculate with excel	2131	FIXME - calculate with excel	14976	FIXME - calculate with excel	00:10:56	00:24:37	00:37:24	99	53	62	0	3	2	840	134" + System.lineSeparator() +
                "Celts	FIXME - calculate with excel	00:55:12	4515	591	4008	520	9634	283	395	0	FIXME [88, 85]	5	72	24468	FIXME - calculate with excel	27178	FIXME - calculate with excel	3038	FIXME - calculate with excel	11157	FIXME - calculate with excel	00:10:56	00:23:32	00:37:48	89	33	38	0	4	2	1102	135"
        assertEquals(expected, convertValues)
    }


}