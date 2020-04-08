package com.captix.http_requests.registration

import com.google.gson.annotations.SerializedName

data class Registration(
    @SerializedName("email") val email: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("password") val password: String
)
