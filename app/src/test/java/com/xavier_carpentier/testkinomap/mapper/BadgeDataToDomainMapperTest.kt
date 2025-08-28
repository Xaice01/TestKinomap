package com.xavier_carpentier.testkinomap.mapper

import com.xavier_carpentier.testkinomap.data.model.BadgeData
import com.xavier_carpentier.testkinomap.data.model.BadgeCategoryData
import com.xavier_carpentier.testkinomap.domain.mapper.toDomain
import com.xavier_carpentier.testkinomap.domain.model.Badge
import com.xavier_carpentier.testkinomap.domain.model.BadgeCategory
import org.junit.Assert.*
import org.junit.Test

class BadgeDataToDomainMapperTest {

    @Test
    fun `maps BadgeData to Domain and computes isUnlocked by percent`() {
        val data = BadgeData(
            id = 10,
            title = "T",
            description = "D",
            category = "C",
            unlockedAtEpochSec = null,
            unlockedPercent = 100,
            imageUnlockedUrl = "u",
            imageLockedUrl = "l"
        )

        val domain: Badge = data.toDomain()

        assertEquals(10, domain.id)
        assertEquals("T", domain.title)
        assertEquals("D", domain.description)
        assertTrue(domain.isUnlocked)
        assertEquals(100, domain.progressPct)
        assertEquals("u", domain.imageUnlockedUrl)
        assertEquals("l", domain.imageLockedUrl)
    }

    @Test
    fun `maps BadgeData to Domain and clamps progressPct`() {
        val data = BadgeData(
            id = 11,
            title = "T",
            description = null,
            category = "C",
            unlockedAtEpochSec = null,
            unlockedPercent = -5, // hors borne
            imageUnlockedUrl = null,
            imageLockedUrl = null
        )

        val d1 = data.toDomain()
        assertEquals(0, d1.progressPct)
        assertFalse(d1.isUnlocked)

        val d2 = data.copy(unlockedPercent = 150).toDomain()
        assertEquals(100, d2.progressPct)
        assertTrue(d2.isUnlocked) // >=100%
    }

    @Test
    fun `maps BadgeCategoryData to Domain and sorts badges by title`() {
        val catData = BadgeCategoryData(
            name = "Cat",
            badges = listOf(
                BadgeData(2, "Zoo", null, "Cat", null, null, null, null),
                BadgeData(1, "alpha", null, "Cat", null, null, null, null)
            )
        )

        val cat: BadgeCategory = catData.toDomain()
        assertEquals("Cat", cat.name)
        assertEquals(2, cat.badges.size)
        assertEquals("alpha", cat.badges[0].title)
        assertEquals("Zoo", cat.badges[1].title)
    }
}
