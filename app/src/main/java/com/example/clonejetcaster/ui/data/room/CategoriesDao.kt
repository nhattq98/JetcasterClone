package com.example.clonejetcaster.ui.data.room

import androidx.room.Dao
import androidx.room.Query
import com.example.clonejetcaster.ui.data.Category
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CategoriesDao : BaseDao<Category> {
    @Query(
        """
        SELECT categories.* FROM categories
        INNER JOIN (
            SELECT category_id, COUNT(podcast_uri) AS podcast_count FROM podcast_category_entries
            GROUP BY category_id
        ) ON category_id = categories.id
        ORDER BY podcast_count DESC
        LIMIT :limit
        """
    )
    abstract fun categoriesSortedByPodcastCount(
        limit: Int,
    ): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE name = :name")
    abstract suspend fun getCategoryWithName(name: String): Category?

}