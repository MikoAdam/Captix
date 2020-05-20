package com.captix.http_requests.comment_request

import com.google.gson.annotations.SerializedName

data class CommentUpload(
    @SerializedName("content") var content: String
)
