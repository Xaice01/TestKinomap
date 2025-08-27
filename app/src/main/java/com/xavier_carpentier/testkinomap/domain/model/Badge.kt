package com.xavier_carpentier.testkinomap.domain.model

data class Badge(
    val id: Int,
    val title: String,
    val description: String?,
    val category: String,
    val unlockedAtEpochSec: Long?,
    val progressPct: Int,
    val imageUnlockedUrl: String?,
    val imageLockedUrl: String?,
    val isUnlocked: Boolean
)