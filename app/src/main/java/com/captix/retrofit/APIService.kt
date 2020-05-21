package com.captix.retrofit

import com.captix.http_requests.comment_request.CommentResponse
import com.captix.http_requests.comment_request.CommentUpload
import com.captix.http_requests.image_request.ImageResponse
import com.captix.http_requests.image_upload.ImageUploadResponse
import com.captix.http_requests.image_upload.ImageUploadSendBack
import com.captix.http_requests.login.LoginRequest
import com.captix.http_requests.login.LoginResponse
import com.captix.http_requests.registration.Registration
import com.captix.http_requests.vote.Vote
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface APIService {

    companion object {
        const val MULTIPART_FORM_DATA = "multipart/form-data"
        const val PHOTO_MULTIPART_KEY_IMG = "file"
    }

    @POST("users")
    fun registrationPost(@Body registration: Registration): Call<Registration>

    @POST("authenticate")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("posts")
    fun getPhotosURL(@Header("Authorization") authHeader: String): Call<List<ImageResponse>>

    @GET
    fun getComments(
        @Header("Authorization") authHeader: String,
        @Url url: String
    ): Call<List<CommentResponse>>

    @POST
    fun sendComment(
        @Header("Authorization") authHeader: String,
        @Url url: String,
        @Body content: CommentUpload
    ): Call<CommentResponse>

    @POST
    fun sendVote(
        @Header("Authorization") authHeader: String,
        @Url url: String,
        @Body vote: Vote
    ): Call<Vote>

    @Multipart
    @POST("uploadFile")
    fun uploadImage(
        @Header("Authorization") authHeader: String,
        @Part file: MultipartBody.Part
    ): Call<ImageUploadResponse>

    @POST("posts")
    fun uploadImageAnswer(
        @Header("Authorization") authHeader: String,
        @Body answer: ImageUploadSendBack
    ): Call<ImageUploadSendBack>

}

object ApiUtils {

    private const val BASE_URL = "http://18.184.252.89:8080/"

    val apiService: APIService
        get() = RetrofitClient.getClient(BASE_URL)!!.create(
            APIService::class.java
        )
}