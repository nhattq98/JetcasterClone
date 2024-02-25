package com.example.clonejetcaster.ui.category

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.clonejetcaster.ui.data.PodcastWithExtraInfo
import com.example.clonejetcaster.utils.ToggleFollowPodcastIconButton

@Composable
private fun CategoryPodcastRow(
    podcasts: List<PodcastWithExtraInfo>,
    onTogglePodcastFollowed: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lastIndex = podcasts.size - 1
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
    ) {
        itemsIndexed(items = podcasts) { index, (podcast, _, isFollowed): PodcastWithExtraInfo ->
            TopPodcastRowItem(
                podcastTitle = podcast.title,
                isFollowed = isFollowed,
                podcastImageUrl = podcast.imageUrl,
                onToggleFollowClicked = { onTogglePodcastFollowed(podcast.uri) },
                modifier = Modifier.width(128.dp)
            )
        }
    }
}

@Composable
private fun TopPodcastRowItem(
    podcastTitle: String,
    isFollowed: Boolean,
    modifier: Modifier,
    onToggleFollowClicked: (() -> Unit)? = null,
    podcastImageUrl: String? = null,
) {
    Column {
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .align(Alignment.CenterHorizontally)
        ) {
            if (podcastImageUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(podcastImageUrl)
                        .crossfade(true).build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.medium)
                )
            }
            ToggleFollowPodcastIconButton(
                isFollowed = isFollowed,
                onClick = { /*TODO*/ },
                modifier = Modifier.align(
                    Alignment.BottomEnd
                )
            )
        }

        Text(
            text = podcastTitle,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun previewTopPodcastItemRow() {
    TopPodcastRowItem(
        podcastTitle = "PodcastTitle",
        isFollowed = false,
        modifier = Modifier.width(128.dp)
    )
}