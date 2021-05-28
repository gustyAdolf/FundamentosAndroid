package com.simplegapps.fundamentosandroid.topics

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import com.simplegapps.fundamentosandroid.databinding.ActivityTopicsBinding
import com.simplegapps.fundamentosandroid.di.DIProvider

class TopicsActivity : AppCompatActivity() {

    private val binding: ActivityTopicsBinding by lazy {
        ActivityTopicsBinding.inflate(
            layoutInflater
        )
    }
    private val topicsAdapter = TopicsAdapter()
    private val viewModel: TopicsViewModel by viewModels { DIProvider.topicsViewModelProviderFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.topics.apply {
            adapter = topicsAdapter
            addItemDecoration(DividerItemDecoration(this@TopicsActivity, LinearLayout.VERTICAL))
        }

        viewModel.state.observe(this) {
            when (it) {
                is TopicsViewModel.State.LoadingTopics -> renderLoading(it)
                is TopicsViewModel.State.TopicsReceived -> topicsAdapter.submitList(it.topics)
                is TopicsViewModel.State.NoTopics -> renderEmptyState()
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadTopics()
            binding.swipeRefreshLayout.isRefreshing = false
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.loadTopics()
    }

    private fun renderEmptyState() {
        binding.noTopics.isVisible = true
        binding.noTopics.text = "No se han encontrado topics"
    }

    private fun renderLoading(loadingState: TopicsViewModel.State.LoadingTopics) {
        (loadingState as? TopicsViewModel.State.LoadingTopics.LoadingWithTopics)?.let {
            topicsAdapter.submitList(it.topics)
        }
    }

    companion object {
        @JvmStatic
        fun createIntent(context: Context): Intent = Intent(context, TopicsActivity::class.java)
    }

}