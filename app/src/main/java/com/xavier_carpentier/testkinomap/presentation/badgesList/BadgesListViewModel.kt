package com.xavier_carpentier.testkinomap.presentation.badgesList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xavier_carpentier.testkinomap.domain.useCase.GetAllCategoryUseCase
import com.xavier_carpentier.testkinomap.presentation.mapper.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BadgesListViewModel @Inject constructor(
    private val getAllCategoryUseCase: GetAllCategoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<BadgesListUiState>(BadgesListUiState.Loading)
    val state: StateFlow<BadgesListUiState> = _state

    init { loadCategories() }

    fun loadCategories() {
        viewModelScope.launch {
            _state.value = BadgesListUiState.Loading
            runCatching { getAllCategoryUseCase() }
                .onSuccess { categories ->
                    if (categories.isEmpty()) {
                        _state.value = BadgesListUiState.Empty
                    } else {
                        _state.value = BadgesListUiState.Success(
                            categories = categories.map { it.toUi() }
                        )
                    }
                }
                .onFailure {
                    _state.value = BadgesListUiState.Empty
                }
        }
    }
}