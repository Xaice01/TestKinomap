package com.xavier_carpentier.testkinomap.presentation.badge

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.xavier_carpentier.testkinomap.domain.model.Badge
import com.xavier_carpentier.testkinomap.domain.model.BadgeCategory
import com.xavier_carpentier.testkinomap.domain.repository.BadgeRepository
import com.xavier_carpentier.testkinomap.domain.useCase.GetBadgeUseCase
import com.xavier_carpentier.testkinomap.presentation.badges.BadgeDetailUiState
import com.xavier_carpentier.testkinomap.presentation.badges.BadgeDetailViewModel
import com.xavier_carpentier.testkinomap.presentation.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class FakeRepo(
    private val badges: List<Badge> = emptyList(),
    private val throwOnFetch: Boolean = false
) : BadgeRepository {

    override suspend fun fetchCategories(): List<BadgeCategory> {
        if (throwOnFetch) error("boom")
        return listOf(BadgeCategory(name = "Any", badges = badges))
    }

    override suspend fun getBadgeById(id: Int): Badge? {
        if (throwOnFetch) error("boom")
        return badges.find { it.id == id }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class BadgeDetailViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before fun setup() { Dispatchers.setMain(dispatcher) }
    @After fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun `Success when badge exists and is unlocked`() = runTest {
        val unlocked = Badge(
            id = 2,
            title = "Mont Blanc 4809m",
            description = "4809m total ascent",
            category = "Activity",
            unlockedAtEpochSec = 1_728_657_430L,
            progressPct = 100,
            imageUnlockedUrl = "u.png",
            imageLockedUrl = "l.png",
            isUnlocked = true
        )
        val vm = BadgeDetailViewModel(
            getBadgeUseCase = GetBadgeUseCase(FakeRepo(listOf(unlocked))),
            savedStateHandle = SavedStateHandle(mapOf(Screen.BadgeDetail.ARG_BADGE_ID to 2))
        )

        dispatcher.scheduler.advanceUntilIdle()

        val s = vm.state.value
        assertThat(s is BadgeDetailUiState.Success).isTrue()
        s as BadgeDetailUiState.Success
        assertThat(s.badge.id).isEqualTo(2)
        assertThat(s.badge.isUnlocked).isTrue()
        assertThat(s.badge.imageUrlToShow).isEqualTo("u.png")
        assertThat(s.badge.unlockedDateLabel).isNotNull()
        assertThat(s.badge.subtitleCategory).isEqualTo("Activity")
        assertThat(s.badge.progressPct).isEqualTo(100)
    }

    @Test
    fun `Success when badge exists and is locked`() = runTest {
        val locked = Badge(
            id = 40,
            title = "Finisher of the Connected Marathon Pour Tous",
            description = "Finisher of the Paris 2024 Connected Marathon Pour Tous Challenge",
            category = "Activity",
            unlockedAtEpochSec = null, // null => pas débloqué
            progressPct = 0,
            imageUnlockedUrl = "u-locked.png",
            imageLockedUrl = "l-locked.png",
            isUnlocked = false
        )
        val vm = BadgeDetailViewModel(
            getBadgeUseCase = GetBadgeUseCase(FakeRepo(listOf(locked))),
            savedStateHandle = SavedStateHandle(mapOf(Screen.BadgeDetail.ARG_BADGE_ID to 40))
        )

        dispatcher.scheduler.advanceUntilIdle()

        val s = vm.state.value
        assertThat(s is BadgeDetailUiState.Success).isTrue()
        s as BadgeDetailUiState.Success
        assertThat(s.badge.id).isEqualTo(40)
        assertThat(s.badge.isUnlocked).isFalse()
        assertThat(s.badge.imageUrlToShow).isEqualTo("l-locked.png")
        assertThat(s.badge.unlockedDateLabel).isNull()
        assertThat(s.badge.progressPct).isEqualTo(0)
    }

    @Test
    fun `Empty when badge not found`() = runTest {
        val vm = BadgeDetailViewModel(
            getBadgeUseCase = GetBadgeUseCase(FakeRepo(emptyList())),
            savedStateHandle = SavedStateHandle(mapOf(Screen.BadgeDetail.ARG_BADGE_ID to 99))
        )

        dispatcher.scheduler.advanceUntilIdle()

        assertThat(vm.state.value).isEqualTo(BadgeDetailUiState.Empty)
    }

    @Test
    fun `Empty when repository throws`() = runTest {
        val vm = BadgeDetailViewModel(
            getBadgeUseCase = GetBadgeUseCase(FakeRepo(throwOnFetch = true)),
            savedStateHandle = SavedStateHandle(mapOf(Screen.BadgeDetail.ARG_BADGE_ID to 2))
        )

        dispatcher.scheduler.advanceUntilIdle()

        assertThat(vm.state.value).isEqualTo(BadgeDetailUiState.Empty)
    }

    @Test(expected = IllegalStateException::class)
    fun `throws at construction when badgeId nav arg is missing`() {
        BadgeDetailViewModel(
            getBadgeUseCase = GetBadgeUseCase(FakeRepo(emptyList())),
            savedStateHandle = SavedStateHandle()
        )
    }
}