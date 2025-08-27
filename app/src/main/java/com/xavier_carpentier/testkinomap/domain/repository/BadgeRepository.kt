package com.xavier_carpentier.testkinomap.domain.repository

import com.xavier_carpentier.testkinomap.domain.model.Badge
import com.xavier_carpentier.testkinomap.domain.model.BadgeCategory

interface BadgeRepository {
    suspend fun fetchCategories(): List<BadgeCategory>
    suspend fun getBadgeById(id: Int): Badge?
}