package com.xavier_carpentier.testkinomap.datasource.remote

import com.google.common.truth.Truth.assertThat
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.xavier_carpentier.testkinomap.datasource.remote.api.BadgeApi
import com.xavier_carpentier.testkinomap.datasource.remote.dto.BadgesResponseDto
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class BadgeApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: BadgeApi

    private val json = Json { ignoreUnknownKeys = true; explicitNulls = false }

    @Before
    fun setUp() {
        server = MockWebServer()
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
        api = retrofit.create(BadgeApi::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `getBadges parses minimal response`() = runTest {
        val body = """
            {
              "data": [
                {
                  "name": "Activity",
                  "badges": [
                    {
                      "id": 2,
                      "name": "Everest 8848m",
                      "description": "8848m total ascent",
                      "action": "activity_8848everest",
                      "category": "Activity",
                      "unlocked_date": 1728657430,
                      "unlocked_percent": 100,
                      "images_url": {
                        "unlocked": "https://static.kinomap.com/badges/activity_8848everest-win.png",
                        "locked": "https://static.kinomap.com/badges/activity_8848everest.png"
                      }
                    }
                  ]
                }
              ],
              "unlocked_content": []
            }
        """.trimIndent()

        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        val res: BadgesResponseDto = api.getBadges("TOKEN")
        assertThat(res.data).hasSize(1)
        val cat = res.data.first()
        assertThat(cat.name).isEqualTo("Activity")
        assertThat(cat.badges).hasSize(1)
        val badge = cat.badges.first()
        assertThat(badge.id).isEqualTo(2)
        assertThat(badge.name).contains("Everest")
        assertThat(badge.images_url.unlocked).contains("everest-win")
    }
}