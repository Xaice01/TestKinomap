package com.xavier_carpentier.testkinomap.presentation.badges

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.xavier_carpentier.testkinomap.R
import com.xavier_carpentier.testkinomap.presentation.composeUtils.EmptyScreen
import com.xavier_carpentier.testkinomap.presentation.composeUtils.LoadingScreen
import com.xavier_carpentier.testkinomap.presentation.composeUtils.ScreenType
import com.xavier_carpentier.testkinomap.presentation.composeUtils.getScreenType
import com.xavier_carpentier.testkinomap.presentation.model.BadgeUi
import com.xavier_carpentier.testkinomap.presentation.theme.TestKinomapTheme

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun BadgeDetailScreen(
    onBack: () -> Unit,
    vm: BadgeDetailViewModel = hiltViewModel()
) {
    val uiState by vm.state.collectAsState()
    val windowSizeClass = LocalActivity.current?.let { calculateWindowSizeClass(it) }
        ?: throw IllegalStateException(stringResource(R.string.context_is_not_an_activity))
    val screenType = getScreenType(windowSizeClass)

    when(uiState){
        is BadgeDetailUiState.Loading -> LoadingScreen()
        is BadgeDetailUiState.Empty -> EmptyScreen(text = R.string.empty_badges)
        is BadgeDetailUiState.Success -> {
            val badge = (uiState as BadgeDetailUiState.Success).badge
            when(screenType){
                ScreenType.Compact, ScreenType.Medium  -> BadgeDetailCompactContent(badge = badge)
                ScreenType.Expanded -> BadgeDetailExpandedContent(badge = badge)
            }

        }
    }
}

@Composable
fun BadgeDetailExpandedContent(badge: BadgeUi) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text=badge.title,
            Modifier
                .paddingFromBaseline(top = 40.dp, bottom = 8.dp)
                .padding(start = 16.dp),
            style = MaterialTheme.typography.displaySmall
        )
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
            tonalElevation = 2.dp,
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = stringResource(R.string.category, badge.subtitleCategory),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(6.dp))

        Row(modifier = Modifier, horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp,end = 12.dp)) {
                badge.description?.let {
                    Text(
                        text=it,
                        modifier = Modifier.padding(horizontal = 12.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))

                val label = if (badge.isUnlocked) {
                    stringResource(R.string.badge_unlocked_on, badge.unlockedDateLabel ?: stringResource(R.string.unknown_date))
                } else {
                    stringResource(R.string.badge_progress, badge.progressPct)
                }
                Text(
                    text = label,
                    modifier = Modifier.padding(horizontal = 12.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            AsyncImage(
                model = badge.imageUrlToShow,
                contentDescription = stringResource(R.string.contentDescriptionAsyncImage),
                modifier = Modifier
                    .size(180.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
                error = painterResource(id = R.drawable.ic_launcher_background)
            )
        }



    }
}

@Composable
fun BadgeDetailCompactContent(
    badge : BadgeUi
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text=badge.title,
            Modifier
                .paddingFromBaseline(top = 40.dp, bottom = 8.dp)
                .padding(start = 16.dp),
            style = MaterialTheme.typography.displaySmall
        )
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
            tonalElevation = 2.dp,
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = stringResource(R.string.category, badge.subtitleCategory),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(6.dp))

        Row(modifier = Modifier, horizontalArrangement = Arrangement.SpaceBetween) {
            badge.description?.let {
                Text(
                    text=it,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp,end = 12.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            AsyncImage(
                model = badge.imageUrlToShow,
                contentDescription = stringResource(R.string.contentDescriptionAsyncImage),
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
                error = painterResource(id = R.drawable.ic_launcher_background)
            )
        }

        val label = if (badge.isUnlocked) {
            stringResource(R.string.badge_unlocked_on, badge.unlockedDateLabel ?: stringResource(R.string.unknown_date))
        } else {
            stringResource(R.string.badge_progress, badge.progressPct)
        }
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 12.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 600)
@Composable
fun BadgeDetailCompactContentPreview(){
    TestKinomapTheme {
        BadgeDetailCompactContent(
            badge = BadgeUi(
                id = 40,
                title = "Finisher of the Connected Marathon Pour Tous",
                subtitleCategory = "Activity",
                description = "Finisher of the Paris 2024 Connected Marathon Pour Tous Challenge",
                progressPct = 0,
                isUnlocked = false,
                imageUrlToShow = "https://static.kinomap.com/badges/activity_finisher_mptc.png",
                unlockedDateLabel = null
            )
        )
    }
}

@Preview(showBackground = true, widthDp = 840, heightDp = 600)
@Composable
fun BadgeDetailExpandedContentPreview(){
    TestKinomapTheme {
        BadgeDetailExpandedContent(
            badge = BadgeUi(
                id = 40,
                title = "Finisher of the Connected Marathon Pour Tous",
                subtitleCategory = "Activity",
                description = "Finisher of the Paris 2024 Connected Marathon Pour Tous Challenge",
                progressPct = 40,
                isUnlocked = false,
                imageUrlToShow = "https://static.kinomap.com/badges/activity_finisher_mptc.png",
                unlockedDateLabel = null
            )
        )
    }
}