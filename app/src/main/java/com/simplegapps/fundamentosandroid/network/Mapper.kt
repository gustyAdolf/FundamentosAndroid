package com.simplegapps.fundamentosandroid.network

import com.simplegapps.fundamentosandroid.model.LogIn
import com.simplegapps.fundamentosandroid.model.Post
import com.simplegapps.fundamentosandroid.model.Topic
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

fun Response.toSignInModel(): LogIn = when (this.isSuccessful) {
    true -> LogIn.Success(
        JSONObject(this.body?.string()).getJSONObject("user").getString("username")
    )
    false -> LogIn.Error(this.body?.string() ?: "Some Error parsing response")
}

fun IOException.toSignInModel(): LogIn = LogIn.Error(this.toString())

fun Response.toSignUpModel(): LogIn = when (this.isSuccessful) {
    true -> LogIn.Success(
        JSONObject(this.body?.string()).getJSONObject("user").getString("username")
    )
    false -> LogIn.Error(this.body?.string() ?: "Some Error parsing response")
}

fun IOException.toSignUpModel(): LogIn = LogIn.Error(this.toString())

fun Response.toTopicsModel(): Result<List<Topic>> = when (this.isSuccessful) {
    true -> Result.success(parseTopics(body?.string()))
        .also { println("JcLog: BackendResult -> $it") }
    false -> Result.failure(IOException(this.body?.string() ?: "Some Error parsing response"))
}

fun Response.toTopicDetailsModel(): Result<List<Post>> = when (this.isSuccessful) {
    true -> Result.success(parseTopicDetails(body?.string()))
        .also { println("JcLog: BackendResult -> $it") }
    false -> Result.failure(IOException(this.body?.string() ?: "Some Error parsing response"))
}

fun IOException.toTopicsModel(): Result<List<Topic>> = Result.failure(this)
fun IOException.toTopicDetailsModel(): Result<List<Post>> = Result.failure(this)

fun parseTopics(json: String?): List<Topic> = json?.let {
    val topicsJsonArray: JSONArray =
        JSONObject(it).getJSONObject("topic_list").getJSONArray("topics")
    (0 until topicsJsonArray.length()).map { index ->
        val topicJsonObject = topicsJsonArray.getJSONObject(index)
        Topic(
            id = topicJsonObject.getInt("id"),
            title = topicJsonObject.getString("title"),
            replyCount = topicJsonObject.getInt("reply_count"),
            likeCount = topicJsonObject.getInt("like_count"),
            pinned = topicJsonObject.getBoolean("pinned"),
            bumped = topicJsonObject.getBoolean("bumped"),
            lastPostedAt = topicJsonObject.getString("last_posted_at"),
            lastPosterUser = topicJsonObject.getString("last_poster_username")
        )
    }
} ?: emptyList<Topic>()


fun parseTopicDetails(json: String?): List<Post> = json?.let {
    val postsJsonArray: JSONArray =
        JSONObject(it).getJSONObject("post_stream").getJSONArray("posts")
    (0 until postsJsonArray.length()).map { index ->
        val post = postsJsonArray.getJSONObject(index)
        Post(
            username = post.getString("username"),
            cooked = post.getString("cooked")
                .replace("<p>", "")
                .replace("</p>", "")
        )
    }
} ?: emptyList<Post>()


