package com.captix.http_requests.image_request

import com.captix.model.Comment
import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("_id") var _id: String,
    @SerializedName("username") var username: String,
    @SerializedName("imageUrl") var imageUrl: String,
    @SerializedName("caption") var caption: String,
    @SerializedName("description") var description: String?,
    @SerializedName("dateOfCreation") var dateOfCreation: String,
    @SerializedName("comments") var comments: MutableList<Comment>? = mutableListOf(),
    @SerializedName("upvote") var upvote: Int,
    @SerializedName("downvote") var downvote: Int,
    @SerializedName("categoryName") var categoryName: String
)