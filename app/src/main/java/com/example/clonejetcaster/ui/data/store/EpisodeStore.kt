package com.example.clonejetcaster.ui.data.store

import com.example.clonejetcaster.ui.data.Episode
import com.example.clonejetcaster.ui.data.room.EpisodesDao
import kotlinx.coroutines.flow.Flow


class EpisodeStore(
    private val episodesDao: EpisodesDao,
) {
    fun episodeWithUri(episodeUri: String): Flow<Episode> {
        return episodesDao.episode(episodeUri)
    }

    fun episodesInPodcast(
        podcastUri: String,
        limit: Int = Integer.MAX_VALUE,
    ): Flow<List<Episode>> {
        return episodesDao.episodesForPodcastUri(podcastUri, limit)
    }

    suspend fun addEpisodes(episodes: Collection<Episode>) = episodesDao.insertAll(episodes)

    suspend fun isEmpty(): Boolean = episodesDao.count() == 0
}
