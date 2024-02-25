package com.example.clonejetcaster.ui.data.repository

import com.example.clonejetcaster.ui.data.PodcastRssResponse
import com.example.clonejetcaster.ui.data.PodcastsFetcher
import com.example.clonejetcaster.ui.data.preview.SampleFeeds
import com.example.clonejetcaster.ui.data.room.TransactionRunner
import com.example.clonejetcaster.ui.data.store.CategoryStore
import com.example.clonejetcaster.ui.data.store.EpisodeStore
import com.example.clonejetcaster.ui.data.store.PodcastStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PodcastsRepository(
    private val podcastsFetcher: PodcastsFetcher,
    private val podcastStore: PodcastStore,
    private val episodeStore: EpisodeStore,
    private val categoryStore: CategoryStore,
    private val transactionRunner: TransactionRunner,
    mainDispatcher: CoroutineDispatcher,
) {
    private var refreshingJob: Job? = null

    private val scope = CoroutineScope(mainDispatcher)

    suspend fun updatePodcasts(force: Boolean) {
        if (refreshingJob?.isActive == true) {
            refreshingJob?.join()
        } else if (force || podcastStore.isEmpty()) {
            refreshingJob = scope.launch {
                podcastsFetcher(SampleFeeds)
                    .filter { it is PodcastRssResponse.Success }
                    .map { it as PodcastRssResponse.Success }
                    .collect { (podcast, episodes, categories) ->
                        transactionRunner {
                            podcastStore.addPodcast(podcast)
                            episodeStore.addEpisodes(episodes)

                            categories?.forEach { category ->
                                val categoryId = categoryStore.addCategory(category)
                                categoryStore.addPodcastToCategory(
                                    podcastUri = podcast.uri,
                                    categoryId = categoryId
                                )
                            }
                        }
                    }
            }
        }
    }
}