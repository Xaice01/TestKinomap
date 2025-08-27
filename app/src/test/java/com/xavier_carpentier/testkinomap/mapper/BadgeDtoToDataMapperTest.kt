package com.xavier_carpentier.testkinomap.mapper

import com.xavier_carpentier.testkinomap.data.mapper.toData
import com.xavier_carpentier.testkinomap.data.model.BadgeCategoryData
import com.xavier_carpentier.testkinomap.data.model.BadgeData
import com.xavier_carpentier.testkinomap.datasource.remote.dto.BadgeDto
import com.xavier_carpentier.testkinomap.datasource.remote.dto.CategoryDto
import com.xavier_carpentier.testkinomap.datasource.remote.dto.ImagesUrlDto
import org.junit.Assert.*
import org.junit.Test

class BadgeDtoToDataMapperTest {

    @Test
    fun `maps BadgeDto to BadgeData fully`() {
        val dto = BadgeDto(
            id = 40,
            name = "Finisher",
            description = "desc",
            action = "some_action",
            category = "Activity",
            unlocked_date = 1735555987L,
            unlocked_percent = 100,
            images_url = ImagesUrlDto(
                unlocked = "https://u.png",
                locked = "https://l.png"
            )
        )

        val data: BadgeData = dto.toData()

        assertEquals(40, data.id)
        assertEquals("Finisher", data.title)
        assertEquals("desc", data.description)
        assertEquals("Activity", data.category)
        assertEquals(1735555987L, data.unlockedAtEpochSec)
        assertEquals(100, data.unlockedPercent)
        assertEquals("https://u.png", data.imageUnlockedUrl)
        assertEquals("https://l.png", data.imageLockedUrl)
    }

    @Test
    fun `maps CategoryDto to BadgeCategoryData and sorts by title`() {
        val dto = CategoryDto(
            name = "Activity",
            badges = listOf(
                BadgeDto(2, "Zeta", null, null, "Activity", null, null, ImagesUrlDto(null, null)),
                BadgeDto(1, "alpha", null, null, "Activity", null, null, ImagesUrlDto(null, null))
            )
        )

        val data: BadgeCategoryData = dto.toData()

        assertEquals("Activity", data.name)
        assertEquals(2, data.badges.size)
        assertEquals("alpha", data.badges[0].title)
        assertEquals("Zeta", data.badges[1].title)
    }

    @Test
    fun `handles nulls gracefully`() {
        val dto = BadgeDto(
            id = 3,
            name = "Name",
            description = null,
            action = null,
            category = "Cat",
            unlocked_date = null,
            unlocked_percent = null,
            images_url = ImagesUrlDto(unlocked = null, locked = null)
        )

        val data = dto.toData()

        assertEquals(3, data.id)
        assertNull(data.description)
        assertNull(data.unlockedAtEpochSec)
        assertNull(data.unlockedPercent)
        assertNull(data.imageUnlockedUrl)
        assertNull(data.imageLockedUrl)
    }
}