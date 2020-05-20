package com.captix.http_requests.comment_request

import com.google.gson.annotations.SerializedName

data class CommentResponse(
    @SerializedName("_id") var _id: String,
    @SerializedName("username") var username: String?,
    @SerializedName("content") var content: String,
    @SerializedName("postId") var postId: String
)