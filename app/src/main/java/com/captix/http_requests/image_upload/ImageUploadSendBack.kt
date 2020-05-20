package com.captix.http_requests.image_upload

import com.google.gson.annotations.SerializedName

data class ImageUploadSendBack(
    @SerializedName("username") var username: String,
    @SerializedName("imageUrl") var imageUrl: String,
    @SerializedName("caption") var caption: String,
    @SerializedName("description") var description: String,
    @SerializedName("categoryName") var categoryName: String
)