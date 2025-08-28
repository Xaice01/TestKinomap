package com.xavier_carpentier.testkinomap.domain.useCase

import com.xavier_carpentier.testkinomap.domain.model.BadgeCategory
import com.xavier_carpentier.testkinomap.domain.repository.BadgeRepository
import javax.inject.Inject


class GetAllCategoryUseCase @Inject constructor(
    private val repository: BadgeRepository
) {
    suspend operator fun invoke(): List<BadgeCategory> = repository.fetchCategories()
}