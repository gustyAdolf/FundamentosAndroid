package com.simplegapps.fundamentosandroid.repository

import com.simplegapps.fundamentosandroid.model.LogIn
import com.simplegapps.fundamentosandroid.model.Post
import com.simplegapps.fundamentosandroid.model.Topic
import com.simplegapps.fundamentosandroid.network.Client

class Repository(private val client: Client) {
    private var userName: String = ""

    fun signIn(
        userName: String,
        password: String,
        callback: Callback<LogIn>
    ) {
        client.signIn(userName, password) {
            storeLogIn(it)
            callback.onResult(it)
        }
    }

    fun signup(
        username: String,
        email: String,
        password: String,
        callback: Callback<LogIn>,
    ) {
        client.signUp(username, email, password) {
            callback.onResult(it)
        }
    }

    fun getTopics(callback: Callback<Result<List<Topic>>>) {
        client.getTopics {
            callback.onResult(it)
        }
    }

    fun getTopicDetail(topicId: Int, callback: Callback<Result<List<Post>>>) {
        client.getTopicDetails(topicId) {
            callback.onResult(it)
        }
    }

    private fun storeLogIn(logIn: LogIn) {
        userName = (logIn as? LogIn.Success)?.userName ?: userName
    }

    fun interface Callback<T> {
        fun onResult(t: T)
    }
}