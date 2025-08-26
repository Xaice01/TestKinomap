package com.xavier_carpentier.testkinomap.presentation.model

data class BadgeUi(
    val id: String,
    val title: String,
    val subtitleCategory: String,
    val description: String?,
    val progressPct: Int,
    val isUnlocked: Boolean,
    val imageUrlToShow: String?, // déjà résolu (locked/unlocked)
    val unlockedDateLabel: String? // ex: "Débloqué le : 2025-03-21"
)