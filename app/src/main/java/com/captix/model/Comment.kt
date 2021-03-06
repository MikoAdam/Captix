package com.captix.model

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("_id") var _id: String?,
    @SerializedName("username") var username: String?,
    @SerializedName("content") var content: String?
)