package com.xavier_carpentier.testkinomap.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.xavier_carpentier.testkinomap.domain.model.Badge
import com.xavier_carpentier.testkinomap.domain.model.BadgeCategory
import com.xavier_carpentier.testkinomap.domain.repository.BadgeRepository
import com.xavier_carpentier.testkinomap.domain.useCase.GetAllCategoryUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Test

private class FakeBadgeRepository(
    private val categories: List<BadgeCategory>
) : BadgeRepository {
    override suspend fun fetchCategories(): List<BadgeCategory> = categories
    override suspend fun getBadgeById(id: Int): Badge? =
        categories.asSequence().flatMap { it.badges.asSequence() }.firstOrNull { it.id == id }
}

class GetAllCategoryUseCaseTest {

    @Test
    fun `returns categories from repository`() = runTest {
        // Given
        val c1 = BadgeCategory(
            name = "Activity",
            badges = listOf(
                Badge(1, "Everest", null, "Activity", 1_728_657_430L, 100, "u.png", "l.png", true)
            )
        )
        val c2 = BadgeCategory(
            name = "Social",
            badges = listOf(
                Badge(2, "Photogenic", null, "Social", null, 0, "u2.png", "l2.png", false)
            )
        )
        val repo = FakeBadgeRepository(listOf(c1, c2))
        val useCase = GetAllCategoryUseCase(repo)

        // When
        val result = useCase()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Activity")
        assertThat(result[1].badges.map { it.id }).containsExactly(2)
    }

    @Test
    fun `returns empty list when repository has no categories`() = runTest {
        val repo = FakeBadgeRepository(emptyList())
        val useCase = GetAllCategoryUseCase(repo)

        val result = useCase()

        assertThat(result).isEmpty()
    }
}