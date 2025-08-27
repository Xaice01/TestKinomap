package com.xavier_carpentier.testkinomap.di

import com.xavier_carpentier.testkinomap.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TokenModule {

    @Provides @Singleton
    @Named("appToken")
    fun provideAppToken(): String = BuildConfig.KINOMAP_APP_TOKEN
}