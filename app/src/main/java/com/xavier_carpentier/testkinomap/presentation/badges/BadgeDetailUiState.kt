package com.xavier_carpentier.testkinomap.presentation.badges

import com.xavier_carpentier.testkinomap.presentation.model.BadgeUi

sealed interface BadgeDetailUiState {
    data object Empty : BadgeDetailUiState      // avant chargement / pas d’ID
    data object Loading : BadgeDetailUiState
    data class Success(
        val badge: BadgeUi
    ) : BadgeDetailUiState
}