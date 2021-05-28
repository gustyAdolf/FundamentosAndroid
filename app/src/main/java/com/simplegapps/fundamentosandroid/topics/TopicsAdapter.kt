package com.simplegapps.fundamentosandroid.topics

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simplegapps.fundamentosandroid.databinding.ViewTopicBinding
import com.simplegapps.fundamentosandroid.extensions.inflater
import com.simplegapps.fundamentosandroid.model.Topic
import com.simplegapps.fundamentosandroid.topicsdetails.TopicDetailsActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TopicsAdapter(
    difUtilItemCallback: DiffUtil.ItemCallback<Topic> = DIFF
) :
    ListAdapter<Topic, TopicsAdapter.TopicViewHolder>(difUtilItemCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        return TopicViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) =
        holder.bind(getItem(position))


    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Topic>() {
            override fun areItemsTheSame(oldItem: Topic, newItem: Topic): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Topic, newItem: Topic): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class TopicViewHolder(
        parent: ViewGroup,
        private val binding: ViewTopicBinding = ViewTopicBinding.inflate(
            parent.inflater,
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(topic: Topic) {
            binding.title.text = topic.title
            binding.replies.text = topic.replyCount.toString()
            binding.likes.text = topic.likeCount.toString()
            binding.topicDate.text = changeDateStringFormat(topic.lastPostedAt)
            binding.username.text = topic.lastPosterUser
            itemView.setOnClickListener {
                itemView.context.startActivity(
                    TopicDetailsActivity.createIntent(
                        itemView.context,
                        topic.id
                    )
                )
            }

            if (topic.bumped) {
                binding.pinnedBumb.isVisible = true
                binding.pinnedBumb.text = "bumped"
            }
            if (topic.pinned) {
                binding.pinnedBumb.isVisible = true
                binding.pinnedBumb.text = "pinned"
            }
        }

        private fun changeDateStringFormat(stringDate: String): String {
            val apiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val date = LocalDate.parse(stringDate, apiFormat)
            val newFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            return date.format(newFormat)
        }

    }


}