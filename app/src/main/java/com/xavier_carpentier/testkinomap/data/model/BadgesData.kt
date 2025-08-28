package com.xavier_carpentier.testkinomap.data.model

data class BadgeData(
    val id: Int,
    val title: String,
    val description: String?,
    val category: String,
    val unlockedAtEpochSec: Long?,
    val unlockedPercent: Int?,
    val imageUnlockedUrl: String?,
    val imageLockedUrl: String?
)