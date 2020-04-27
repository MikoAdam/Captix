package com.captix.http_requests.image_request

import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("_id") var id: String,
    @SerializedName("title") var title: String,
    @SerializedName("url") var url: String
)