package com.xavier_carpentier.testkinomap.presentation.model

data class BadgeUi(
    val id: Int,
    val title: String,
    val subtitleCategory: String,
    val description: String?,
    val progressPct: Int,
    val isUnlocked: Boolean,
    val imageUrlToShow: String?,
    val unlockedDateLabel: String? 
)