package com.brijwel.zohotask.ui.feeds

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.brijwel.zohotask.R
import com.brijwel.zohotask.databinding.FragmentFeedsBinding
import com.brijwel.zohotask.domain.model.Post
import com.brijwel.zohotask.ui.adapter.PostAdapter
import com.brijwel.zohotask.utils.SpaceDecoration
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FeedsFragment : Fragment(R.layout.fragment_feeds) {

    private val viewModel by viewModels<FeedsViewModel>()

    private val postAdapter = PostAdapter { post: Post ->
        viewModel.toggleFavouriteStatus(post)
    }
    private var sortByMenuItem: MenuItem? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().setTitle(R.string.forum)
        setHasOptionsMenu(true)
        val binding = FragmentFeedsBinding.bind(view)
        binding.rvPosts.adapter = postAdapter
        binding.rvPosts.addItemDecoration(
            SpaceDecoration(
                0,
                resources.getDimensionPixelSize(R.dimen.margin_small),
                0,
                resources.getDimensionPixelSize(R.dimen.margin_small)
            )
        )
        binding.swipeRefresh.setOnRefreshListener { viewModel.refreshPosts() }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.posts.collectLatest {
                        postAdapter.submitData(it)
                    }
                }
                launch {
                    viewModel.refreshState.collect { event ->
                        when (event) {
                            is RefreshState.Error -> {
                                Snackbar.make(binding.root, event.error, Snackbar.LENGTH_SHORT)
                                    .show()
                            }
                            is RefreshState.Loading -> {
                                binding.swipeRefresh.isRefreshing = event.isLoading
                            }
                        }
                    }
                }
            }
        }
        viewModel.sortAtoZ.observe(viewLifecycleOwner) { isAToZ ->
            sortByMenuItem?.icon = ContextCompat.getDrawable(
                requireContext(),
                if (isAToZ) R.drawable.ic_sort_a_z
                else R.drawable.ic_sort_z_a
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        sortByMenuItem = menu.getItem(0)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sort) {
            viewModel.toggleSortBy()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        sortByMenuItem = null
        super.onDestroyView()
    }
}