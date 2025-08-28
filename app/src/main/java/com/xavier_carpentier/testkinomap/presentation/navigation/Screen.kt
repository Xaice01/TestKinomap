package com.xavier_carpentier.testkinomap.presentation.navigation

import androidx.annotation.StringRes
import com.xavier_carpentier.testkinomap.R

sealed class Screen(val route: String, @StringRes val titleRes: Int) {

    data object Badges : Screen("badges", R.string.screen_badges)
    data object BadgeDetail : Screen("badge/{badgeId}", R.string.screen_badge_detail) {
        const val ARG_BADGE_ID = "badgeId"
        fun route(id: Int) = "badge/$id"
    }
}