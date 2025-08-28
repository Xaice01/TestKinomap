package com.xavier_carpentier.testkinomap.presentation.badgesList

import com.xavier_carpentier.testkinomap.presentation.model.BadgeCategoryUi

sealed interface BadgesListUiState {
    data object Empty : BadgesListUiState
    data object Loading : BadgesListUiState
    data class Success(
        val categories: List<BadgeCategoryUi>
    ) : BadgesListUiState
}