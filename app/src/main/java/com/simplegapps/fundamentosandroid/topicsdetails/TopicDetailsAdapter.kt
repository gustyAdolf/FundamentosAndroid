package com.simplegapps.fundamentosandroid.topicsdetails

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simplegapps.fundamentosandroid.databinding.ViewTopicDetailsBinding
import com.simplegapps.fundamentosandroid.extensions.inflater
import com.simplegapps.fundamentosandroid.model.Post

class TopicDetailsAdapter(difUtilItemCallback: DiffUtil.ItemCallback<Post> = DIFF) :
    ListAdapter<Post, TopicDetailsAdapter.TopicDetailsViewHolder>(difUtilItemCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopicDetailsAdapter.TopicDetailsViewHolder {
        return TopicDetailsAdapter.TopicDetailsViewHolder(parent)
    }

    override fun onBindViewHolder(
        holder: TopicDetailsAdapter.TopicDetailsViewHolder,
        position: Int
    ) =
        holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.cooked == newItem.cooked
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }
        }
    }

    class TopicDetailsViewHolder(
        parent: ViewGroup,
        private val binding: ViewTopicDetailsBinding = ViewTopicDetailsBinding.inflate(
            parent.inflater,
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.username.text = post.username
            binding.title.text = post.cooked
        }
    }
}