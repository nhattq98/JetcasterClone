package com.example.clonejetcaster.ui.data.store

import com.example.clonejetcaster.ui.data.Podcast
import com.example.clonejetcaster.ui.data.PodcastFollowedEntry
import com.example.clonejetcaster.ui.data.PodcastWithExtraInfo
import com.example.clonejetcaster.ui.data.room.PodcastFollowedEntryDao
import com.example.clonejetcaster.ui.data.room.PodcastsDao
import com.example.clonejetcaster.ui.data.room.TransactionRunner
import kotlinx.coroutines.flow.Flow

/**
 * data repostitory fod [podcast] instance
 */
class PodcastStore(
    private val podcastDao: PodcastsDao,
    private val podcastFollowedEntryDao: PodcastFollowedEntryDao,
    private val transactionRunner: TransactionRunner,
) {
    fun podcastsWithUri(uri: String): Flow<Podcast> = podcastDao.podcastWithUri(uri)

    /**
     * return a flow containing a list of all followed podcasts, sorted by last episode date
     */
    fun podcastsSortedByLastEpisode(
        limit: Int = Int.MAX_VALUE,
    ): Flow<List<PodcastWithExtraInfo>> = podcastDao.podcastsSortedByLastEpisode(limit)

    fun followedPodcastSortedByLastEpisode(limit: Int = Int.MAX_VALUE): Flow<List<PodcastWithExtraInfo>> =
        podcastDao.followedPodcastsSortedByLastEpisode(limit = limit)

    private suspend fun followPodcast(podcastUri: String) {
        podcastFollowedEntryDao.insert(PodcastFollowedEntry(podcastUri = podcastUri))
    }

    suspend fun unfollowPodcast(podcastUri: String) {
        podcastFollowedEntryDao.deleteWithPodcastUri(podcastUri)
    }

    suspend fun togglePodcastFollowed(podcastUri: String) = transactionRunner {
        podcastUri.let {
            if (podcastFollowedEntryDao.isPodcastFollowed(it)) {
                unfollowPodcast(it)
            } else {
                followPodcast(it)
            }
        }
    }

    suspend fun addPodcast(podcast: Podcast) {
        podcastDao.insert(podcast)
    }

    suspend fun isEmpty(): Boolean = podcastDao.count() == 0
}