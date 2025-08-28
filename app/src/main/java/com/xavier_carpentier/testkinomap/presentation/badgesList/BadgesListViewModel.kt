package com.xavier_carpentier.testkinomap.presentation.badgesList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xavier_carpentier.testkinomap.domain.useCase.GetAllCategoryUseCase
import com.xavier_carpentier.testkinomap.presentation.mapper.toUi
import com.xavier_carpentier.testkinomap.presentation.model.BadgeCategoryUi
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

    private var sourceCategories: List<BadgeCategoryUi> = emptyList()

    init { loadCategories() }

    fun loadCategories() {
        viewModelScope.launch {
            _state.value = BadgesListUiState.Loading
            runCatching { getAllCategoryUseCase() }
                .onSuccess { categories ->
                    if (categories.isEmpty()) {
                        _state.value = BadgesListUiState.Empty
                    } else {
                        sourceCategories = categories.map { it.toUi() }
                        _state.value = BadgesListUiState.Success(
                            categories = sourceCategories,
                            query = ""
                        )
                    }
                }
                .onFailure {
                    _state.value = BadgesListUiState.Empty
                }
        }
    }

    fun onQueryChange(newQuery: String) {
        val current = _state.value
        if (current !is BadgesListUiState.Success) return

        val q = newQuery.trim().lowercase().normalize()
        val filtered =
            if (q.isEmpty()) sourceCategories
            else sourceCategories.mapNotNull { cat ->
                val badges = cat.badges.filter { it.title.lowercase().normalize().contains(q) }
                if (badges.isEmpty()) null else cat.copy(badges = badges)
            }

        _state.value = if (filtered.isEmpty()) {
            BadgesListUiState.Success(categories = emptyList(), query = newQuery)
        } else {
            BadgesListUiState.Success(categories = filtered, query = newQuery)
        }
    }
}

// petite extension pour ignorer les accents (ex: “é” ~ “e”)
private fun String.normalize(): String =
    java.text.Normalizer.normalize(this, java.text.Normalizer.Form.NFD)
        .replace("\\p{Mn}+".toRegex(), "")
