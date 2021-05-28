package com.simplegapps.fundamentosandroid.network

import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class RequestBuilder(
    private val baseUrl: String,
    private val apiKey: String
) {
    fun signInRequest(userName: String): Request = createGetRequest(userEndpoint(userName))

    fun signUpRequest(username: String, email: String, password: String): Request =
        createPostRequest(
            signUpEndpoint(),
            signUpBodyRequest(username, email, password)
        )

    fun topicsRequest(): Request = createGetRequest(topicsEndpoint())
    fun topicDetailsRequest(topicId: Int): Request = createGetRequest(topicDetailsEndpoint(topicId))
    fun userRequest(username: String): Request = createGetRequest(userEndpoint(username))
    private fun signUpEndpoint(): HttpUrl = buildHttpUrl("users")
    private fun topicsEndpoint(): HttpUrl = buildHttpUrl("latest.json")
    private fun topicDetailsEndpoint(topicId: Int): HttpUrl = buildHttpUrl("t/$topicId.json")
    private fun userEndpoint(userName: String): HttpUrl = buildHttpUrl("users/$userName.json")

    private fun createAuthenticatedPostRequest(
        httpUrl: HttpUrl,
        body: RequestBody,
        transform: Request.Builder.() -> Unit = { }
    ): Request = createPostRequest(httpUrl, body) {
        header("Api-Key", apiKey)
        transform()
    }

    fun createPostRequest(
        httpUrl: HttpUrl,
        body: RequestBody,
        transform: Request.Builder.() -> Unit = {}
    ): Request = createRequest(httpUrl) {
        post(body)
        header("Content-Type", "application/json")
        header("Accept", "application/json")
        transform()
    }

    private fun createGetRequest(httpUrl: HttpUrl): Request =
        createRequest(httpUrl, Request.Builder::get)

    fun createRequest(httpUrl: HttpUrl): Request = createRequest(httpUrl, Request.Builder::get)

    private fun createRequest(
        httpUrl: HttpUrl,
        transform: Request.Builder.() -> Unit = {}
    ): Request = Request.Builder()
        .url(httpUrl)
        .apply(transform)
        .build()

    private fun buildHttpUrl(pathSegments: String): HttpUrl =
        HttpUrl.Builder()
            .scheme("https")
            .host(baseUrl)
            .addPathSegments(pathSegments)
            .build()

    companion object {
        private val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
        private fun signUpBodyRequest(
            username: String,
            email: String,
            password: String
        ): RequestBody = JSONObject()
            .put("name", username)
            .put("username", username)
            .put("email", email)
            .put("password", password)
            .put("active", true)
            .put("approved", true)
            .toString()
            .toRequestBody(JSON)
    }
}