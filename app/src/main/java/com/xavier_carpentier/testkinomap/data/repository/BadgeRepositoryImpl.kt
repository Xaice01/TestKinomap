package com.xavier_carpentier.testkinomap.data.repository

import com.xavier_carpentier.testkinomap.data.mapper.toData
import com.xavier_carpentier.testkinomap.datasource.remote.api.BadgeApi
import com.xavier_carpentier.testkinomap.domain.mapper.toDomain
import com.xavier_carpentier.testkinomap.domain.model.Badge
import com.xavier_carpentier.testkinomap.domain.model.BadgeCategory
import com.xavier_carpentier.testkinomap.domain.repository.BadgeRepository
import javax.inject.Inject

class BadgeRepositoryImpl @Inject constructor(
    private val api: BadgeApi,
    private val token: String
) : BadgeRepository {

    private var cache: List<BadgeCategory>? = null

    override suspend fun fetchCategories(): List<BadgeCategory> {
        cache?.let { return it }
        val res = api.getBadges(token)
        val data = res.data.map { it.toData() }
        return data.map { it.toDomain() }
            .sortedBy { it.name.lowercase() }
            .also { cache = it }
    }

    override suspend fun getBadgeById(id: Int): Badge? =
        fetchCategories().asSequence().flatMap { it.badges.asSequence() }.firstOrNull { it.id == id }
}