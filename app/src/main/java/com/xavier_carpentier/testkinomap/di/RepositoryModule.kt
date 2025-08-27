package com.xavier_carpentier.testkinomap.di

import com.xavier_carpentier.testkinomap.BuildConfig
import com.xavier_carpentier.testkinomap.data.repository.BadgeRepositoryImpl
import com.xavier_carpentier.testkinomap.datasource.remote.api.BadgeApi
import com.xavier_carpentier.testkinomap.domain.repository.BadgeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides @Singleton @Named("appToken")
    fun provideAppToken(): String = BuildConfig.KINOMAP_APP_TOKEN

    @Provides @Singleton
    fun provideBadgeRepository(
        api: BadgeApi,
        @Named("appToken") token: String
    ): BadgeRepository = BadgeRepositoryImpl(api, token)
}