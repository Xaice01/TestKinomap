package com.xavier_carpentier.testkinomap.presentation.badgesList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.xavier_carpentier.testkinomap.R
import com.xavier_carpentier.testkinomap.presentation.composeUtils.EmptyScreen
import com.xavier_carpentier.testkinomap.presentation.composeUtils.LoadingScreen
import com.xavier_carpentier.testkinomap.presentation.model.BadgeCategoryUi
import com.xavier_carpentier.testkinomap.presentation.model.BadgeUi
import com.xavier_carpentier.testkinomap.presentation.theme.TestKinomapTheme
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun BadgesListScreen(
    onBadgeClick: (Int) -> Unit,
    vm: BadgesListViewModel = hiltViewModel()
) {
    val uiState by vm.state.collectAsState()

    when(uiState){
        is BadgesListUiState.Loading -> LoadingScreen()
        is BadgesListUiState.Empty -> EmptyScreen(text = R.string.empty_badges)
        is BadgesListUiState.Success -> {
            val categories = uiState as BadgesListUiState.Success
            BadgesListByCategories(
                categories = categories.categories,
                onBadgeClick = onBadgeClick
            )
        }
    }
}

@Composable
fun BadgesListByCategories(
    categories: List<BadgeCategoryUi>,
    onBadgeClick: (Int) -> Unit
){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            ListBadge(
                nameCategory = category.name,
                listBadge = category.badges,
                onBadgeClick = onBadgeClick
            )
        }
    }
}

@Composable
fun ListBadge(
    nameCategory: String,
    listBadge: List<BadgeUi>,
    onBadgeClick: (Int) -> Unit
) {
    Column {
        Text(
            modifier = Modifier.padding(8.dp),
            text = nameCategory,
            style = MaterialTheme.typography.titleMedium
        )
        LazyRow(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listBadge) { badge ->
                BadgeItem(
                    badge = badge,
                    onBadgeClick = onBadgeClick
                )
            }
        }
    }

}

@Composable
fun BadgeItem(
    badge: BadgeUi,
    onBadgeClick: (Int) -> Unit
){
    Card(modifier = Modifier.size(width = 100.dp, height = 130.dp),
        elevation = CardDefaults.cardElevation(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        onClick = {onBadgeClick(badge.id)}
    ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BadgeImageWithProgress(
                    imageUrl = badge.imageUrlToShow,
                    progress = badge.progressPct / 100f
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = badge.title,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
            }
    }
}

@Composable
private fun BadgeImageWithProgress(
    imageUrl: String?,
    progress: Float,
) {
    val size = 65.dp
    val ringStroke = 4.dp
    Box(
        modifier = Modifier.size(size + ringStroke * 2),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            strokeWidth = ringStroke,
            modifier = Modifier.size(size + ringStroke * 2),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        AsyncImage(
            model = imageUrl,
            contentDescription = R.string.contentDescriptionAsyncImage.toString(),
            modifier = Modifier
                .size(size)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
            error = painterResource(id = R.drawable.ic_launcher_background)
        )
    }
}

@Preview
@Composable
fun BadgeItemPreview() {
    TestKinomapTheme {
        BadgeItem(
            badge = BadgeUi(
                id = 40,
                title = "Finisher of the Connected Marathon Pour Tous",
                subtitleCategory = "Activity",
                description = "Finisher of the Paris 2024 Connected Marathon Pour Tous Challenge",
                progressPct = 40,
                isUnlocked = false,
                imageUrlToShow = "https://static.kinomap.com/badges/activity_finisher_mptc.png",
                unlockedDateLabel = null
            ),
            onBadgeClick = { }
        )
    }
}

@Preview(widthDp = 200, heightDp = 800,showBackground = true)
@Composable
fun ListBadgePreview() {
    val fakeBadges = listOf(
        BadgeUi(
            id = 40,
            title = "Finisher of the Connected Marathon Pour Tous",
            subtitleCategory = "Activity",
            description = "Finisher of the Paris 2024 Connected Marathon Pour Tous Challenge",
            progressPct = 40,
            isUnlocked = false,
            imageUrlToShow = "https://static.kinomap.com/badges/activity_finisher_mptc.png",
            unlockedDateLabel = null
        ),
        BadgeUi(
            id = 36,
            title = "First multiplayer activity",
            subtitleCategory = "Activity",
            description = "First multiplayer activity done on Kinomap",
            progressPct = 0,
            isUnlocked = true,
            imageUrlToShow = "https://static.kinomap.com/badges/activity_1stmulti-win.png",
            unlockedDateLabel = "Débloqué le : 2024-12-31"
        ),
        BadgeUi(
            id = 2,
            title = "Mont Blanc 4809m",
            subtitleCategory = "Activity",
            description = "4809m total ascent",
            progressPct = 100,
            isUnlocked = true,
            imageUrlToShow = "https://static.kinomap.com/badges/activity_4809montblanc-win.png",
            unlockedDateLabel = "Débloqué le : 2024-11-10"
        )
    )

    TestKinomapTheme {
        ListBadge(
            nameCategory = "Activity",
            listBadge = fakeBadges,
            onBadgeClick = {}
        )
    }
}

