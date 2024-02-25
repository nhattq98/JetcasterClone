package com.example.clonejetcaster.ui.data.room

import androidx.room.Dao
import androidx.room.Query
import com.example.clonejetcaster.ui.data.Episode
import com.example.clonejetcaster.ui.data.EpisodeToPodcast
import kotlinx.coroutines.flow.Flow

@Dao
abstract class EpisodesDao : BaseDao<Episode> {
    @Query("SELECT * FROM episodes WHERE uri = :uri")
    abstract fun episode(uri: String): Flow<Episode>

    @Query("SELECT * FROM episodes WHERE podcast_uri = :podcastUri ORDER BY datetime(published) DESC LIMIT :limit")
    abstract fun episodesForPodcastUri(podcastUri: String, limit: Int): Flow<List<Episode>>

    @Query("SELECT COUNT(*) FROM episodes")
    abstract suspend fun count(): Int

    @Query(
        """
        SELECT episodes.* FROM episodes
        INNER JOIN podcast_category_entries ON episodes.podcast_uri = podcast_category_entries.podcast_uri
        WHERE category_id = :categoryId
        ORDER BY datetime(published) DESC
        LIMIT :limit
    """
    )
    abstract fun episodesFromPodcastsInCategory(
        categoryId: Long,
        limit: Int,
    ): Flow<List<EpisodeToPodcast>>
}