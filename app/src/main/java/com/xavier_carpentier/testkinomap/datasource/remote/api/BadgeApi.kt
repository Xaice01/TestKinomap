package com.xavier_carpentier.testkinomap.datasource.remote.api

import com.xavier_carpentier.testkinomap.datasource.remote.dto.BadgesResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface BadgeApi {
    @GET("v4/badges/mobile-tech-test")
    suspend fun getBadges(
        @Query("appToken") token: String
    ): BadgesResponseDto
}