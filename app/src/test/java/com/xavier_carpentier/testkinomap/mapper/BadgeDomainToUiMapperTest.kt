package com.xavier_carpentier.testkinomap.mapper

import com.xavier_carpentier.testkinomap.domain.model.Badge
import com.xavier_carpentier.testkinomap.domain.model.BadgeCategory
import com.xavier_carpentier.testkinomap.presentation.mapper.toUi
import com.xavier_carpentier.testkinomap.presentation.model.BadgeCategoryUi
import com.xavier_carpentier.testkinomap.presentation.model.BadgeUi
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

class BadgeDomainToUiMapperTest {

    private lateinit var previousLocale: Locale
    private lateinit var previousTz: TimeZone

    @Before
    fun setup() {
        previousLocale = Locale.getDefault()
        previousTz = TimeZone.getDefault()
        Locale.setDefault(Locale.FRANCE)
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    @After
    fun tearDown() {
        Locale.setDefault(previousLocale)
        TimeZone.setDefault(previousTz)
    }

    @Test
    fun `maps locked badge with progress and formats date label null`() {
        val domain = Badge(
            id = 1,
            title = "Name",
            description = "Desc",
            category = "Activity",
            unlockedAtEpochSec = null,
            progressPct = 42,
            imageUnlockedUrl = "https://u",
            imageLockedUrl = "https://l",
            isUnlocked = false
        )

        val ui: BadgeUi = domain.toUi()

        assertEquals(1, ui.id)
        assertEquals("Name", ui.title)
        assertEquals("Activity", ui.subtitleCategory)
        assertEquals("Desc", ui.description)
        assertEquals(42, ui.progressPct)
        assertFalse(ui.isUnlocked)
        assertEquals("https://l", ui.imageUrlToShow)
        assertNull(ui.unlockedDateLabel)
    }

    @Test
    fun `maps unlocked badge and formats unlocked date`() {
        val epoch = 1_734_035_200L // 2024-12-12 00:00:00 UTC
        val domain = Badge(
            id = 2,
            title = "Name2",
            description = null,
            category = "Travel",
            unlockedAtEpochSec = epoch,
            progressPct = 100,
            imageUnlockedUrl = "https://u2",
            imageLockedUrl = "https://l2",
            isUnlocked = true
        )

        val ui: BadgeUi = domain.toUi()

        assertTrue(ui.isUnlocked)
        assertEquals("https://u2", ui.imageUrlToShow)
        assertEquals("12 déc. 2024", ui.unlockedDateLabel)
    }

    @Test
    fun `maps category to CategoryUi`() {
        val cat = BadgeCategory(
            name = "Activity",
            badges = listOf(
                Badge(
                    id = 1,
                    title = "A",
                    description = null,
                    category = "Activity",
                    unlockedAtEpochSec = null,
                    progressPct = 0,
                    imageUnlockedUrl = null,
                    imageLockedUrl = null,
                    isUnlocked = false
                )
            )
        )
        val ui: BadgeCategoryUi = cat.toUi()
        assertEquals("Activity", ui.name)
        assertEquals(1, ui.badges.size)
        assertEquals("A", ui.badges[0].title)
    }
}
