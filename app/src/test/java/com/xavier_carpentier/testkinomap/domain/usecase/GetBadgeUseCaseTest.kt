package com.xavier_carpentier.testkinomap.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.xavier_carpentier.testkinomap.domain.model.Badge
import com.xavier_carpentier.testkinomap.domain.model.BadgeCategory
import com.xavier_carpentier.testkinomap.domain.repository.BadgeRepository
import com.xavier_carpentier.testkinomap.domain.useCase.GetBadgeUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Test

private class FakeBadgeRepository2(
    private val categories: List<BadgeCategory>
) : BadgeRepository {
    override suspend fun fetchCategories(): List<BadgeCategory> = categories
    override suspend fun getBadgeById(id: Int): Badge? =
        categories.asSequence().flatMap { it.badges.asSequence() }.firstOrNull { it.id == id }
}

class GetBadgeUseCaseTest {

    @Test
    fun `returns badge when id exists`() = runTest {
        // Given
        val target = Badge(10, "Everest 8848m", "desc", "Activity", 1_728_657_430L, 100, "u.png", "l.png", true)
        val cats = listOf(
            BadgeCategory("Activity", listOf(target)),
            BadgeCategory("Social", listOf(Badge(99, "Photogenic", null, "Social", null, 0, null, null, false)))
        )
        val repo = FakeBadgeRepository2(cats)
        val useCase = GetBadgeUseCase(repo)

        // When
        val result = useCase(10)

        // Then
        assertThat(result).isNotNull()
        assertThat(result!!.title).contains("Everest")
        assertThat(result.isUnlocked).isTrue()
    }

    @Test
    fun `returns null when id does not exist`() = runTest {
        val cats = listOf(
            BadgeCategory("Activity", listOf(Badge(1, "X", null, "Activity", null, 0, null, null, false)))
        )
        val repo = FakeBadgeRepository2(cats)
        val useCase = GetBadgeUseCase(repo)

        val result = useCase(404)

        assertThat(result).isNull()
    }
}