package com.simplegapps.fundamentosandroid.topicsdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import com.simplegapps.fundamentosandroid.databinding.ActivityTopicDetailsBinding
import com.simplegapps.fundamentosandroid.di.DIProvider

class TopicDetailsActivity : AppCompatActivity() {

    private val binding: ActivityTopicDetailsBinding by lazy {
        ActivityTopicDetailsBinding.inflate(layoutInflater)
    }
    private val viewModel: TopicDetailsViewModel by viewModels { DIProvider.topicDetailsViewModelProvider }

    private val topicsDetailsAdapter = TopicDetailsAdapter()

    var topicId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var intent = intent
        topicId = intent.getIntExtra("topicId", 0)

        binding.topicDetails.apply {
            adapter = topicsDetailsAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this@TopicDetailsActivity,
                    LinearLayout.VERTICAL
                )
            )
        }
        binding.viewLoading.root.isVisible = true
        viewModel.state.observe(this) {
            when (it) {
                is TopicDetailsViewModel.State.LoadingPost -> renderLoading(it)
                is TopicDetailsViewModel.State.TopicDetailsReceived -> {
                    topicsDetailsAdapter.submitList(it.posts)
                    binding.viewLoading.root.isVisible = false
                }
                is TopicDetailsViewModel.State.NoPost -> {
                    renderEmptyState()
                    binding.viewLoading.root.isVisible = false
                }
            }
        }


    }

    override fun onResume() {
        super.onResume()
        if (topicId != null) {
            viewModel.loadPosts(topicId.toInt())
        }
    }

    private fun renderEmptyState() {
        binding.noPosts.isVisible = true
        binding.noPosts.text = "No se han cargado posts"
    }

    private fun renderLoading(loadingState: TopicDetailsViewModel.State.LoadingPost) {
        (loadingState as? TopicDetailsViewModel.State.LoadingPost.LoadingWithPosts)?.let {
            topicsDetailsAdapter.submitList(it.posts)
        }
    }


    companion object {
        @JvmStatic
        fun createIntent(context: Context, topicId: Int): Intent =
            Intent(context, TopicDetailsActivity::class.java).putExtra("topicId", topicId)
    }
}