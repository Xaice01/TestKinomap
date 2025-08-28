package com.xavier_carpentier.testkinomap.datasource.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class BadgesResponseDto(
    val data: List<CategoryDto>
)

@Serializable
data class CategoryDto(
    val name: String,
    val badges: List<BadgeDto>
)

@Serializable
data class BadgeDto(
    val id: Int,
    val name: String,
    val description: String? = null,
    val action: String? = null,
    val category: String,
    val unlocked_date: Long? = null,     // epoch seconds
    val unlocked_percent: Int? = null,   // 0..100 ou null
    val images_url: ImagesUrlDto
)

@Serializable
data class ImagesUrlDto(
    val unlocked: String? = null,
    val locked: String? = null
)