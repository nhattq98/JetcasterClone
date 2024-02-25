package com.example.clonejetcaster.ui.data.preview

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.clonejetcaster.ui.data.Episode
import com.example.clonejetcaster.ui.data.Podcast
import com.example.clonejetcaster.ui.data.PodcastWithExtraInfo
import java.time.OffsetDateTime
import java.time.ZoneOffset


val PreviewPodcasts = listOf(
    Podcast(
        uri = "fakeUri://podcast/1",
        title = "Android Developers Backstage",
        author = "Android Developers"
    ),
    Podcast(
        uri = "fakeUri://podcast/2",
        title = "Google Developers podcast",
        author = "Google Developers"
    )
)

@RequiresApi(Build.VERSION_CODES.O)
val PreviewPodcastsWithExtraInfo = PreviewPodcasts.mapIndexed { index, podcast ->
    PodcastWithExtraInfo().apply {
        this.podcast = podcast
        this.lastEpisodeDate = OffsetDateTime.now()
        this.isFollowed = index % 2 == 0
    }
}

@RequiresApi(Build.VERSION_CODES.O)
val PreviewEpisodes = listOf(
    Episode(
        uri = "fakeUri://episode/1",
        podcastUri = PreviewPodcasts[0].uri,
        title = "Episode 140: Bubbles!",
        summary = "In this episode, Romain, Chet and Tor talked with Mady Melor and Artur " +
            "Tsurkan from the System UI team about... Bubbles!",
        published = OffsetDateTime.now()
    )
)
