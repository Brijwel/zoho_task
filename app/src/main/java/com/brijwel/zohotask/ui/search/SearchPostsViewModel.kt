package com.brijwel.zohotask.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.brijwel.zohotask.domain.model.Post
import com.brijwel.zohotask.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchPostsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val postRepository: PostRepository
) : ViewModel() {
    val searchQuery = savedStateHandle.getLiveData<String>("search_query")

    val posts = searchQuery.asFlow().flatMapLatest {
        postRepository.getPosts(it).cachedIn(viewModelScope)
    }
    private val matchingCount = searchQuery.asFlow().flatMapLatest {
        postRepository.getMatchingCount(it)
    }

    val resultText = combine(
        searchQuery.asFlow(),
        matchingCount
    ) { query, count ->
        Pair(query, count)
    }.flatMapLatest { (query, count) ->
        flow {
            if (count > 0) emit("""$count results for "$query"""")
            else emit("""No result for "$query"""")
        }
    }.asLiveData()

    fun toggleFavouriteStatus(post: Post) = viewModelScope.launch {
        postRepository.toggleFavoriteStatus(post)
    }
}