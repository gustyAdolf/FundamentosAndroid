package com.simplegapps.fundamentosandroid.di

import android.content.Context
import com.simplegapps.fundamentosandroid.login.LoginViewModel
import com.simplegapps.fundamentosandroid.network.Client
import com.simplegapps.fundamentosandroid.repository.Repository
import com.simplegapps.fundamentosandroid.topics.TopicsViewModel
import com.simplegapps.fundamentosandroid.topicsdetails.TopicDetailsViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object DIProvider {

    private const val DISCOURSE_URL = "mdiscourse.keepcoding.io"
    private const val DISCOURSE_API_KEY =
        "699667f923e65fac39b632b0d9b2db0d9ee40f9da15480ad5a4bcb3c1b095b7a"

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    private val client: Client by lazy { Client(DISCOURSE_URL, DISCOURSE_API_KEY, okHttpClient) }

    private val repository: Repository by lazy { Repository(client) }

    val loginViewModelProviderFactory: LoginViewModel.LoginViewModelProviderFactory by lazy {
        LoginViewModel.LoginViewModelProviderFactory(
            repository
        )
    }

    val topicsViewModelProviderFactory: TopicsViewModel.TopicsViewModelProviderFactory by lazy {
        TopicsViewModel.TopicsViewModelProviderFactory(
            repository
        )
    }

    val topicDetailsViewModelProvider: TopicDetailsViewModel.TopicDetailViewModelProviderFactory by lazy {
        TopicDetailsViewModel.TopicDetailViewModelProviderFactory(
            repository
        )
    }

    fun init(context: Context) {

    }
}