package com.brijwel.zohotask.domain.repository

import androidx.paging.PagingData
import com.brijwel.zohotask.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun refreshPosts(
        loading: (Boolean) -> Unit,
        error: (String) -> Unit
    )
    fun getPosts(sortAToZ: Boolean): Flow<PagingData<Post>>
    fun getPosts(query: String): Flow<PagingData<Post>>
    fun getMatchingCount(query: String): Flow<Int>
    suspend fun toggleFavoriteStatus(post: Post)
}