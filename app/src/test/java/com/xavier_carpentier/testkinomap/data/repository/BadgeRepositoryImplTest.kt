package com.xavier_carpentier.testkinomap.data.repository

import com.google.common.truth.Truth.assertThat
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.xavier_carpentier.testkinomap.datasource.remote.api.BadgeApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class BadgeRepositoryImplTest {

    private lateinit var server: MockWebServer
    private lateinit var api: BadgeApi
    private lateinit var repo: BadgeRepositoryImpl

    private val json = Json { ignoreUnknownKeys = true; explicitNulls = false }

    @Before
    fun setUp() {
        server = MockWebServer()

        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(client)
            .build()

        api = retrofit.create(BadgeApi::class.java)
        repo = BadgeRepositoryImpl(api, token = "TOKEN")
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `fetchCategories returns mapped domain objects sorted`() = runTest {
        val body = """
            {
              "data": [
                {
                  "name": "Activity",
                  "badges": [
                    { "id": 2, "name": "B-badge", "category": "Activity",
                      "unlocked_date": null, "unlocked_percent": 80,
                      "images_url": {"unlocked": "u.png", "locked": "l.png"} },
                    { "id": 1, "name": "A-badge", "category": "Activity",
                      "unlocked_date": 1728000000, "unlocked_percent": null,
                      "images_url": {"unlocked": "u2.png", "locked": "l2.png"} }
                  ]
                }
              ],
              "unlocked_content": []
            }
        """.trimIndent()

        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        val categories = repo.fetchCategories()
        assertThat(categories).hasSize(1)
        val cat = categories.first()
        assertThat(cat.name).isEqualTo("Activity")


        assertThat(cat.badges.map { it.title }).containsExactly("A-badge", "B-badge").inOrder()


        val a = cat.badges[0]
        val b = cat.badges[1]
        assertThat(a.isUnlocked).isTrue()
        assertThat(b.isUnlocked).isFalse()
    }

    @Test
    fun `getBadgeById returns badge when present`() = runTest {
        val body = """
            { "data": [ { "name": "Activity", "badges": [
                { "id": 10, "name": "X", "category": "Activity",
                  "unlocked_date": null, "unlocked_percent": 100,
                  "images_url": {"unlocked": "u.png", "locked": "l.png"} }
            ] } ], "unlocked_content": [] }
        """.trimIndent()
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        val badge = repo.getBadgeById(10)
        assertThat(badge).isNotNull()
        assertThat(badge!!.id).isEqualTo(10)
        assertThat(badge.isUnlocked).isTrue()
    }
}