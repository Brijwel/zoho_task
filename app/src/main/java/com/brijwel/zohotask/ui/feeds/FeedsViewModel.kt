package com.brijwel.zohotask.ui.feeds

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.brijwel.zohotask.domain.model.Post
import com.brijwel.zohotask.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FeedsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val postRepository: PostRepository
) : ViewModel() {
    init {
        refreshPosts()
    }

    private val _refreshState = MutableSharedFlow<RefreshState>()
    val refreshState: SharedFlow<RefreshState> get() = _refreshState

    val sortAtoZ = savedStateHandle.getLiveData("sort_by", true)
    val posts = sortAtoZ.asFlow().flatMapLatest {
        postRepository.getPosts(it).cachedIn(viewModelScope)
    }

    fun toggleFavouriteStatus(post: Post) = viewModelScope.launch {
        postRepository.toggleFavoriteStatus(post)
    }

    fun toggleSortBy() {
        sortAtoZ.value = !(sortAtoZ.value ?: false)
    }

    fun refreshPosts() = viewModelScope.launch(Dispatchers.IO) {
        postRepository.refreshPosts(
            loading = {
                sentEvent(RefreshState.Loading(it))
            },
            error = {
                sentEvent(RefreshState.Error(it))
            }
        )
    }

    private fun sentEvent(refreshState: RefreshState) = viewModelScope.launch {
        _refreshState.emit(refreshState)
    }
}


