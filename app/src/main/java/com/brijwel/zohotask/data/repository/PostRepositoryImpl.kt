package com.brijwel.zohotask.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.brijwel.zohotask.data.local.PostDao
import com.brijwel.zohotask.data.local.PostEntity
import com.brijwel.zohotask.data.network.ApiService
import com.brijwel.zohotask.domain.model.Post
import com.brijwel.zohotask.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException

class PostRepositoryImpl(
    private val apiService: ApiService,
    private val postDao: PostDao
) : PostRepository {

    override suspend fun refreshPosts(loading: (Boolean) -> Unit, error: (String) -> Unit) {
        try {
            loading(true)
            val posts = apiService.getPosts()
            postDao.insertAll(posts.map { it.mapToPostEntity() })
        } catch (e: Exception) {
            if (e is IOException) {
                error("No internet connection!")
            } else {
                error(e.message ?: "Something went wrong!")
            }
        } finally {
            loading(false)
        }
    }


    override fun getPosts(sortAToZ: Boolean): Flow<PagingData<Post>> {
        return Pager(config = PagingConfig(pageSize = 10)) {
            postDao.getPosts(sortAToZ)
        }.flow.map { pageData: PagingData<PostEntity> ->
            pageData.map { it.mapToPost() }
        }
    }

    override fun getPosts(query: String): Flow<PagingData<Post>> {
        return Pager(config = PagingConfig(pageSize = 10)) {
            postDao.searchPosts(query)
        }.flow.map { pageData: PagingData<PostEntity> ->
            pageData.map { it.mapToPost() }
        }
    }

    override fun getMatchingCount(query: String): Flow<Int> = postDao.getMatchingCount(query)

    override suspend fun toggleFavoriteStatus(post: Post) {
        postDao.toggleFavouriteStatus(post.id)
    }
}