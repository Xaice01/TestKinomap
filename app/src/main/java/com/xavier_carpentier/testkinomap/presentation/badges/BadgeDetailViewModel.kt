package com.xavier_carpentier.testkinomap.presentation.badges

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xavier_carpentier.testkinomap.domain.useCase.GetBadgeUseCase
import com.xavier_carpentier.testkinomap.presentation.mapper.toUi
import com.xavier_carpentier.testkinomap.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BadgeDetailViewModel @Inject constructor(
    private val getBadgeUseCase: GetBadgeUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow<BadgeDetailUiState>(BadgeDetailUiState.Loading)
    val state: StateFlow<BadgeDetailUiState> = _state

    // Nav arg sous forme Int (NavType.IntType dans la route)
    private val badgeId: Int = checkNotNull(savedStateHandle[Screen.BadgeDetail.ARG_BADGE_ID])

    init {
        viewModelScope.launch {
            _state.value = BadgeDetailUiState.Loading
            runCatching { getBadgeUseCase(badgeId) }   // <-- usecase en Int
                .onSuccess { badge ->
                    _state.value = if (badge != null) {
                        BadgeDetailUiState.Success(badge.toUi())
                    } else {
                        BadgeDetailUiState.Empty
                    }
                }
                .onFailure {
                    _state.value = BadgeDetailUiState.Empty
                }
        }
    }
}