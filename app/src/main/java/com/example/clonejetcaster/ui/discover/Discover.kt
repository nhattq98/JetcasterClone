package com.example.clonejetcaster.ui.discover

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.clonejetcaster.R
import com.example.clonejetcaster.ui.data.Episode
import com.example.clonejetcaster.ui.data.Podcast
import com.example.clonejetcaster.ui.data.preview.PreviewEpisodes
import com.example.clonejetcaster.ui.data.preview.PreviewPodcasts
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EpisodeListItem(
    episode: Episode,
    podcast: Podcast,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier = Modifier.clickable { onClick.invoke(episode.uri) }) {
        val (image, episodeTitle, podcastTitle, playIcon, date, overflow) = createRefs()

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(podcast.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(MaterialTheme.shapes.medium)
                .constrainAs(image) {
                    end.linkTo(parent.end, 16.dp)
                    top.linkTo(parent.top, 16.dp)
                }
        )

        Text(
            text = episode.title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.constrainAs(episodeTitle) {
                linkTo(
                    start = parent.start,
                    end = image.start,
                    startMargin = 24.dp,
                    endMargin = 16.dp,
                    bias = 0f,
                )
                top.linkTo(parent.top, 16.dp)
            }
        )

        val titleImageBarrier = createBottomBarrier(podcastTitle, image)

        CompositionLocalProvider(LocalContentColor provides LocalContentColor.current.copy(alpha = 0.4f)) {
            Text(
                text = podcast.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.constrainAs(podcastTitle) {
                    linkTo(
                        start = parent.start,
                        end = image.start,
                        startMargin = 24.dp,
                        endMargin = 16.dp,
                        bias = 0f,
                    )
                    top.linkTo(episodeTitle.bottom, 6.dp)
                    height = Dimension.preferredWrapContent
                    width = Dimension.preferredWrapContent
                }
            )
        }

        Image(
            imageVector = Icons.Rounded.PlayArrow,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false, radius = 24.dp)
                ) {
                    //todo
                }
                .size(48.dp)
                .padding(6.dp)
                .semantics { role = Role.Button }
                .constrainAs(playIcon) {
                    start.linkTo(parent.start, 24.dp)
                    top.linkTo(titleImageBarrier, margin = 10.dp)
                    bottom.linkTo(parent.bottom, 10.dp)
                }

        )

        CompositionLocalProvider(LocalContentColor provides LocalContentColor.current.copy(alpha = 0.4f)) {
            Text(
                text = when {
                    episode.duration != null -> {
                        stringResource(
                            R.string.episode_date_duration,
                            MediumDateFormatter.format(episode.published),
                            episode.duration.toMinutes().toInt()
                        )
                    }

                    else -> MediumDateFormatter.format(episode.published)
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.constrainAs(date) {
                    centerVerticallyTo(playIcon)
                    linkTo(
                        start = playIcon.end,
                        startMargin = 12.dp,
                        end = overflow.start,
                        bias = 0f,
                    )
                    width = Dimension.preferredWrapContent
                }
            )

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.constrainAs(overflow) {
                    end.linkTo(parent.end, 8.dp)
                    centerVerticallyTo(playIcon)
                }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
            }
        }
    }
}

private val MediumDateFormatter by lazy {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    } else {
        TODO("VERSION.SDK_INT < O")
    }
}

@Preview
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun previewEpisodeListItem() {
    EpisodeListItem(
        episode = PreviewEpisodes[0],
        podcast = PreviewPodcasts[0],
        onClick = { },
        modifier = Modifier.fillMaxWidth()
    )
}