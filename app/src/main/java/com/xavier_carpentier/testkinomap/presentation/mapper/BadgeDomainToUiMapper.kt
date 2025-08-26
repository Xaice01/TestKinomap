package com.xavier_carpentier.testkinomap.presentation.mapper

import com.xavier_carpentier.testkinomap.domain.model.Badge
import com.xavier_carpentier.testkinomap.domain.model.BadgeCategory
import com.xavier_carpentier.testkinomap.presentation.model.BadgeCategoryUi
import com.xavier_carpentier.testkinomap.presentation.model.BadgeUi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

fun Long.toLocalDateString(): String {
    val date = Date(this * 1000)
    return dateFormatter.format(date)
}

fun Badge.toUi(): BadgeUi {
    val imageToShow = if (isUnlocked) (imageUnlockedUrl ?: imageLockedUrl)
    else (imageLockedUrl ?: imageUnlockedUrl)
    val unlockedLabel = unlockedAtEpochSec?.toLocalDateString()

    return BadgeUi(
        id = id,
        title = title,
        subtitleCategory = category,
        description = description,
        progressPct = progressPct,
        isUnlocked = isUnlocked,
        imageUrlToShow = imageToShow,
        unlockedDateLabel = unlockedLabel
    )
}

fun BadgeCategory.toUi(): BadgeCategoryUi =
    BadgeCategoryUi(
        name = name,
        badges = badges.map { it.toUi() }
    )