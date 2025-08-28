package com.xavier_carpentier.testkinomap.data.mapper

import com.xavier_carpentier.testkinomap.data.model.BadgeCategoryData
import com.xavier_carpentier.testkinomap.data.model.BadgeData
import com.xavier_carpentier.testkinomap.datasource.remote.dto.BadgeDto
import com.xavier_carpentier.testkinomap.datasource.remote.dto.CategoryDto

fun BadgeDto.toData(): BadgeData = BadgeData(
    id = id,
    title = name,
    description = description,
    category = category,
    unlockedAtEpochSec = unlocked_date,
    unlockedPercent = unlocked_percent,
    imageUnlockedUrl = images_url.unlocked,
    imageLockedUrl = images_url.locked
)

fun CategoryDto.toData(): BadgeCategoryData =
    BadgeCategoryData(
        name = name,
        badges = badges.map { it.toData() }
            .sortedBy { it.title.lowercase() }
    )