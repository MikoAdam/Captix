package com.captix.http_requests.photo_request

import com.google.gson.annotations.SerializedName

data class PhotoURLResponse(
    @SerializedName("_id") var id: String,
    @SerializedName("title") var title: String,
    @SerializedName("url") var url: String
)