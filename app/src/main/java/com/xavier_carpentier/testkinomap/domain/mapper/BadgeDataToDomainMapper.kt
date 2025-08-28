package com.xavier_carpentier.testkinomap.domain.mapper

import com.xavier_carpentier.testkinomap.data.model.BadgeCategoryData
import com.xavier_carpentier.testkinomap.data.model.BadgeData
import com.xavier_carpentier.testkinomap.domain.model.Badge
import com.xavier_carpentier.testkinomap.domain.model.BadgeCategory

fun BadgeData.toDomain(): Badge =
    Badge(
        id = id,
        title = title,
        description = description,
        category = category,
        unlockedAtEpochSec = unlockedAtEpochSec,
        progressPct = unlockedPercent.safePct(),
        imageUnlockedUrl = imageUnlockedUrl,
        imageLockedUrl = imageLockedUrl,
        isUnlocked = (unlockedAtEpochSec != null) || (unlockedPercent.safePct() >= 100)
    )

fun BadgeCategoryData.toDomain(): BadgeCategory =
    BadgeCategory(
        name = name,
        badges = badges.map { it.toDomain() }
            .sortedBy { it.title.lowercase() }
    )

private fun Int?.safePct() = (this ?: 0).coerceIn(0, 100)