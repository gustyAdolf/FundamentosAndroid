package com.simplegapps.fundamentosandroid.model

sealed class LogIn {
    data class Success(val userName: String) : LogIn()
    data class Error(val error: String) : LogIn()
}

data class Topic(
    val id: Int,
    val title: String,
    val replyCount: Int,
    val likeCount: Int,
    val lastPostedAt: String,
    val pinned: Boolean,
    val bumped: Boolean,
    val lastPosterUser: String

)

data class Post(
    val username: String,
    val cooked: String
)
