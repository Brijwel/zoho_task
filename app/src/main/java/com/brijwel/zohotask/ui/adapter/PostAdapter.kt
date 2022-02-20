package com.brijwel.zohotask.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.brijwel.zohotask.databinding.ItemPostBinding
import com.brijwel.zohotask.domain.model.Post

class PostAdapter(
    private val favouriteToggle: (post: Post) -> Unit
) : PagingDataAdapter<Post, PostAdapter.PostViewHolder>(diffUtil) {

    companion object {
        private const val FAVOURITE_STATUS_CHANGES = "FAVOURITE_STATUS_CHANGES"
        private val diffUtil = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem == newItem

            override fun getChangePayload(oldItem: Post, newItem: Post): Any? {
                if (oldItem.isFavourite != newItem.isFavourite) return FAVOURITE_STATUS_CHANGES
                return super.getChangePayload(oldItem, newItem)
            }
        }
    }

    inner class PostViewHolder(
        private val binding: ItemPostBinding,
        favouriteToggle: (post: Post) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.tvLike.setOnClickListener {
                getItem(bindingAdapterPosition)?.let {
                    favouriteToggle(it)
                }
            }
        }

        fun bind(post: Post) {
            binding.apply {
                this.post = post
                setFavourite(post.isFavourite)
                executePendingBindings()
            }
        }

        fun setFavourite(isFavourite: Boolean) {
            binding.isFavourite = isFavourite
        }
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty() && getItem(position) != null) {
            when (payloads[0]) {
                FAVOURITE_STATUS_CHANGES -> {
                    getItem(position)?.let {
                        holder.setFavourite(it.isFavourite)
                    }
                }
            }
        }
        super.onBindViewHolder(holder, position, payloads)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), favouriteToggle
        )
    }
}