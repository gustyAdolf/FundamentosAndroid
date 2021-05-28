package com.simplegapps.fundamentosandroid.topicsdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.simplegapps.fundamentosandroid.model.Post
import com.simplegapps.fundamentosandroid.repository.Repository
import com.simplegapps.fundamentosandroid.topicsdetails.TopicDetailsViewModel.State.TopicDetailsReceived

class TopicDetailsViewModel(private val repository: Repository) : ViewModel() {

    private val _state: MutableLiveData<State> = MutableLiveData<State>().apply {
        postValue(State.LoadingPost.Loading)
    }

    val state: LiveData<State> = _state

    fun loadPosts(topicId: Int) {
        _state.postValue(
            _state.value?.let {
                when (it) {
                    is State.TopicDetailsReceived -> State.LoadingPost.LoadingWithPosts(it.posts)
                    is State.LoadingPost -> it
                    else -> State.LoadingPost.Loading
                }
            })

        repository.getTopicDetail(topicId) {
            it.fold(::onPostReceived, ::onPostFailure)
        }
    }


    private fun onPostReceived(posts: List<Post>) {
        _state.postValue(
            posts.takeUnless { it.isEmpty() }?.let(::TopicDetailsReceived) ?: State.NoPost
        )
    }

    private fun onPostFailure(throwable: Throwable) {
        _state.postValue(State.NoPost)
    }


    sealed class State {
        sealed class LoadingPost : TopicDetailsViewModel.State() {
            object Loading : LoadingPost()
            data class LoadingWithPosts(val posts: List<Post>) : LoadingPost()
        }

        data class TopicDetailsReceived(val posts: List<Post>) : State()
        object NoPost : State()
    }

    class TopicDetailViewModelProviderFactory(private val repository: Repository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            when (modelClass) {
                TopicDetailsViewModel::class.java -> return TopicDetailsViewModel(repository) as T
                else -> throw IllegalArgumentException("TopicDetailViewModelFactory can only create instances of the LoginViewModel")
            }
        }
    }
}