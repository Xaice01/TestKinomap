package com.xavier_carpentier.testkinomap.presentation.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.xavier_carpentier.testkinomap.R
import com.xavier_carpentier.testkinomap.presentation.badges.BadgeDetailScreen
import com.xavier_carpentier.testkinomap.presentation.badgesList.BadgesListScreen
import com.xavier_carpentier.testkinomap.presentation.badgesList.BadgesListUiState
import com.xavier_carpentier.testkinomap.presentation.badgesList.BadgesListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScreen() {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    // Accès au VM de l’écran Badges uniquement quand on est sur la route Badges
    val badgesVm: BadgesListViewModel? =
        if (currentRoute == Screen.Badges.route) {
            backStack?.let { hiltViewModel(it) }
        } else null

    val query: String =
        (badgesVm?.state?.collectAsState()?.value as? BadgesListUiState.Success)?.query ?: ""

    val isOnStart = currentRoute == Screen.Badges.route
    val titleRes = when {
        currentRoute?.startsWith("badge/") == true || currentRoute == Screen.BadgeDetail.route ->
            Screen.BadgeDetail.titleRes
        else -> Screen.Badges.titleRes
    }

    Scaffold(
        topBar = {
            TopAppBar(

                title = {
                    if (currentRoute == Screen.Badges.route) {
                        OutlinedTextField(
                            value = query,
                            onValueChange = { badgesVm?.onQueryChange(it) },
                            singleLine = true,
                            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                            placeholder = { Text(text = stringResource(R.string.search_badges_placeholder)) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
                        )
                    } else {
                        Text(stringResource(id = titleRes))
                    }},
                navigationIcon = {
                    if (!isOnStart) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Badges.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Badges.route) {
                BadgesListScreen(
                    onBadgeClick = { id -> navController.navigate(Screen.BadgeDetail.route(id)) }
                )
            }
            composable(
                route = Screen.BadgeDetail.route,
                arguments = listOf(navArgument(Screen.BadgeDetail.ARG_BADGE_ID) { type = NavType.IntType })
            ) {
                BadgeDetailScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}