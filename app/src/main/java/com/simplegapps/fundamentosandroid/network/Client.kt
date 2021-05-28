package com.simplegapps.fundamentosandroid.network

import com.simplegapps.fundamentosandroid.model.LogIn
import com.simplegapps.fundamentosandroid.model.Post
import com.simplegapps.fundamentosandroid.model.Topic
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class Client(
    baseUrl: String,
    apiKey: String,
    private val okHttpClient: OkHttpClient
) {
    private val requestBuilder = RequestBuilder(baseUrl, apiKey)

    fun signIn(
        userName: String,
        password: String,
        callback: Callback<LogIn>
    ) {
        runRequest(
            requestBuilder.signInRequest(userName),
            callback,
            IOException::toSignInModel,
            Response::toSignInModel,
        )
    }

    fun signUp(
        username: String,
        email: String,
        password: String,
        callback: Callback<LogIn>,
    ) {
        runRequest(
            requestBuilder.signUpRequest(username, email, password),
            callback,
            IOException::toSignUpModel,
            Response::toSignUpModel,
        )
    }

    fun getTopics(callback: Callback<Result<List<Topic>>>) {
        runRequest(
            requestBuilder.topicsRequest(),
            { callback.onResponse(it) },
            IOException::toTopicsModel,
            Response::toTopicsModel,
        )
    }

    fun getTopicDetails(topicId: Int, callback: Callback<Result<List<Post>>>) {
        runRequest(
            requestBuilder.topicDetailsRequest(topicId),
            { callback.onResponse(it) },
            IOException::toTopicDetailsModel,
            Response::toTopicDetailsModel
        )
    }


    private fun <T> runRequest(
        request: Request,
        callback: Callback<T>,
        exceptionTransform: IOException.() -> T,
        responseTransform: Response.() -> T,
    ) {
        okHttpClient.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) =
                callback.onResponse(e.exceptionTransform())

            override fun onResponse(call: Call, response: Response) {
                response.let(responseTransform)?.let { callback.onResponse(it) }
            }
        })
    }

    fun interface Callback<T> {
        fun onResponse(t: T)
    }
}