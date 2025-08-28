package com.xavier_carpentier.testkinomap.presentation.navigation

import com.google.common.truth.Truth.assertThat
import com.xavier_carpentier.testkinomap.R
import org.junit.Test

class ScreenTest {

    @Test
    fun badgeDetail_routeBuilder_buildsCorrectRoute() {
        val route = Screen.BadgeDetail.route(42)
        assertThat(route).isEqualTo("badge/42")
    }

    @Test
    fun badgeDetail_argName_isCorrect() {
        assertThat(Screen.BadgeDetail.ARG_BADGE_ID).isEqualTo("badgeId")
    }

    @Test
    fun titles_areCorrectResIds() {
        assertThat(Screen.Badges.titleRes).isEqualTo(R.string.screen_badges)
        assertThat(Screen.BadgeDetail.titleRes).isEqualTo(R.string.screen_badge_detail)
    }
}