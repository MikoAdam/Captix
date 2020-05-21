package com.captix.http_requests.vote

import com.google.gson.annotations.SerializedName

data class Vote(
    @SerializedName("vote") var vote: String
)