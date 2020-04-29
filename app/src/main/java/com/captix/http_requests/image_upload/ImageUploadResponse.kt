package com.captix.http_requests.image_upload

import com.google.gson.annotations.SerializedName

data class ImageUploadResponse(
    @SerializedName("imageUrl") var imageUrl: String
)