package com.example.clonejetcaster

import android.content.Context
import androidx.room.Room
import com.example.clonejetcaster.ui.data.PodcastsFetcher
import com.example.clonejetcaster.ui.data.repository.PodcastsRepository
import com.example.clonejetcaster.ui.data.room.JetcasterDatabase
import com.example.clonejetcaster.ui.data.room.TransactionRunner
import com.example.clonejetcaster.ui.data.store.CategoryStore
import com.example.clonejetcaster.ui.data.store.EpisodeStore
import com.example.clonejetcaster.ui.data.store.PodcastStore
import com.rometools.rome.io.SyndFeedInput
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File

// simple singleton , in real app we are using DI
object Graph {
    lateinit var okHttpClient: OkHttpClient

    lateinit var database: JetcasterDatabase
        private set

    private val transactionRunner: TransactionRunner
        get() = database.transactionRunnerDao()

    private val syndFeedInput by lazy { SyndFeedInput() }

    val podcastRepository by lazy {
        PodcastsRepository(
            podcastsFetcher = podcastFetcher,
            podcastStore = podcastStore,
            episodeStore = episodeStore,
            categoryStore = categoryStore,
            transactionRunner = transactionRunner,
            mainDispatcher = mainDispatcher
        )
    }

    private val podcastFetcher by lazy {
        PodcastsFetcher(
            okHttpClient = okHttpClient,
            syndFeedInput = syndFeedInput,
            ioDispatcher = ioDispatcher
        )
    }

    val podcastStore by lazy {
        PodcastStore(
            podcastDao = database.podcastsDao(),
            podcastFollowedEntryDao = database.podcastFollowedEntryDao(),
            transactionRunner = transactionRunner
        )
    }

    val episodeStore by lazy {
        EpisodeStore(
            episodesDao = database.episodesDao()
        )
    }

    val categoryStore by lazy {
        CategoryStore(
            categoriesDao = database.categoriesDao(),
            categoryEntryDao = database.podcastCategoryEntryDao(),
            episodesDao = database.episodesDao(),
            podcastsDao = database.podcastsDao()
        )
    }

    private val mainDispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    private val ioDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO

    fun provide(context: Context) {
        okHttpClient = OkHttpClient.Builder()
            .cache(Cache(File(context.cacheDir, "http_cache"), (20 * 1024 * 1024).toLong()))
            .build()

        database = Room.databaseBuilder(context, JetcasterDatabase::class.java, "data.db")
            // This is not recommended for normal apps, but the goal of this sample isn't to
            // showcase all of Room.
            .fallbackToDestructiveMigration()
            .build()
    }
}