package com.xavier_carpentier.testkinomap.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.xavier_carpentier.testkinomap.datasource.remote.api.BadgeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.kinomap.dev/"

    @Provides @Singleton
    fun provideOkHttp(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            .build()

    @Provides @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val json = Json { ignoreUnknownKeys = true; explicitNulls = false }
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(client)
            .build()
    }

    @Provides @Singleton
    fun provideBadgeApi(retrofit: Retrofit): BadgeApi =
        retrofit.create(BadgeApi::class.java)

}