package com.xavier_carpentier.testkinomap.domain.useCase

import com.xavier_carpentier.testkinomap.domain.model.Badge
import com.xavier_carpentier.testkinomap.domain.repository.BadgeRepository
import javax.inject.Inject

/**
 * Return null si not found.
 */
class GetBadgeUseCase @Inject constructor(
    private val repository: BadgeRepository
) {
    suspend operator fun invoke(id: Int): Badge? = repository.getBadgeById(id)
}