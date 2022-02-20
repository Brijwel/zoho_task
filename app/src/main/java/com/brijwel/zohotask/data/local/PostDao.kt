package com.brijwel.zohotask.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(posts: List<PostEntity>)

    @Query("UPDATE post_table SET isFavourite = NOT isFavourite WHERE id=:id ")
    suspend fun toggleFavouriteStatus(id: Int)

    @Query(
        """
        SELECT * FROM post_table ORDER BY
        CASE WHEN :sortAToZ = 1 THEN title END ASC,
        CASE WHEN :sortAToZ = 0 THEN title END DESC
        """
    )
    fun getPosts(sortAToZ: Boolean): PagingSource<Int, PostEntity>

    @Query("SELECT * FROM post_table WHERE title like '%' || :query  || '%' OR body like '%' || :query  || '%'")
    fun searchPosts(query: String): PagingSource<Int, PostEntity>

    @Query("SELECT COUNT(*) FROM post_table WHERE title like '%' || :query  || '%' OR body like '%' || :query  || '%'")
    fun getMatchingCount(query: String): Flow<Int>
}