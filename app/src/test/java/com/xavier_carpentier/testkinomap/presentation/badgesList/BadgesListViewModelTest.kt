package com.xavier_carpentier.testkinomap.presentation.badgesList

import com.google.common.truth.Truth.assertThat
import com.xavier_carpentier.testkinomap.domain.model.Badge
import com.xavier_carpentier.testkinomap.domain.model.BadgeCategory
import com.xavier_carpentier.testkinomap.domain.repository.BadgeRepository
import com.xavier_carpentier.testkinomap.domain.useCase.GetAllCategoryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
class FakeBadgeRepository(
    private val categories: List<BadgeCategory> = emptyList(),
    private val throwOnFetch: Boolean = false
) : BadgeRepository {

    override suspend fun fetchCategories(): List<BadgeCategory> {
        if (throwOnFetch) throw RuntimeException("boom")
        return categories
    }

    override suspend fun getBadgeById(id: Int): Badge? {
        return categories.asSequence().flatMap { it.badges.asSequence() }
            .firstOrNull { it.id == id }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class BadgesListViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before fun setup() { Dispatchers.setMain(dispatcher) }
    @After fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun `on success - state Success with mapped categories`() = runTest {
        val repo = FakeBadgeRepository(
            listOf(
                BadgeCategory(
                    name = "Activity",
                    badges = listOf(
                        Badge(
                            id = 1,
                            title = "Everest",
                            description = null,
                            category = "Activity",
                            unlockedAtEpochSec = null,
                            progressPct = 0,
                            imageUnlockedUrl = null,
                            imageLockedUrl = "l.png",
                            isUnlocked = false
                        )
                    )
                )
            )
        )
        val useCase = GetAllCategoryUseCase(repo)
        val vm = BadgesListViewModel(useCase)

        // Avance le dispatcher pour exécuter init{ loadCategories() }
        dispatcher.scheduler.advanceUntilIdle()

        val state = vm.state.value
        assertThat(state).isInstanceOf(BadgesListUiState.Success::class.java)
        val success = state as BadgesListUiState.Success
        assertThat(success.categories).hasSize(1)
        assertThat(success.categories[0].name).isEqualTo("Activity")
        assertThat(success.categories[0].badges[0].title).isEqualTo("Everest")
    }

    @Test
    fun `empty repo - state Empty`() = runTest {
        val useCase = GetAllCategoryUseCase(FakeBadgeRepository(emptyList()))
        val vm = BadgesListViewModel(useCase)
        dispatcher.scheduler.advanceUntilIdle()

        assertThat(vm.state.value).isEqualTo(BadgesListUiState.Empty)
    }

    @Test
    fun `repo throws - state Empty`() = runTest {
        val useCase = GetAllCategoryUseCase(FakeBadgeRepository(emptyList(), throwOnFetch = true))
        val vm = BadgesListViewModel(useCase)
        dispatcher.scheduler.advanceUntilIdle()

        assertThat(vm.state.value).isEqualTo(BadgesListUiState.Empty)
    }
}