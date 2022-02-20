package com.brijwel.zohotask.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.brijwel.zohotask.R
import com.brijwel.zohotask.databinding.FragmentSearchPostsBinding
import com.brijwel.zohotask.domain.model.Post
import com.brijwel.zohotask.ui.adapter.PostAdapter
import com.brijwel.zohotask.utils.SpaceDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchPostsFragment : Fragment(R.layout.fragment_search_posts) {

    private val viewModel by viewModels<SearchPostsViewModel>()

    private val postAdapter = PostAdapter { post: Post ->
        viewModel.toggleFavouriteStatus(post)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSearchPostsBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.rvPosts.addItemDecoration(
            SpaceDecoration(
                0,
                resources.getDimensionPixelSize(R.dimen.margin_small),
                0,
                resources.getDimensionPixelSize(R.dimen.margin_small)
            )
        )
        binding.rvPosts.adapter = postAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.posts.collectLatest {
                    postAdapter.submitData(it)
                }
            }
        }
    }
}