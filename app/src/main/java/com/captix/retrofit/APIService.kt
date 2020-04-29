package com.captix.retrofit

import com.captix.http_requests.image_request.ImageResponse
import com.captix.http_requests.image_upload.ImageUploadResponse
import com.captix.http_requests.image_upload.ImageUploadSendBack
import com.captix.http_requests.login.LoginRequest
import com.captix.http_requests.login.LoginResponse
import com.captix.http_requests.registration.Registration
import okhttp3.MultipartBody
import okhttp3.Response
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
    fun getPhotosURL(/*@Body token: String*/): Call<List<ImageResponse>>

    @Multipart
    @POST("uploadFile")
    fun uploadImage(@Part file: MultipartBody.Part): Call<ImageUploadResponse>

    @POST("posts")
    fun uploadImageAnswer(@Body answer: ImageUploadSendBack): Call<ImageUploadSendBack>

}

object ApiUtils {

    private const val BASE_URL = "http://18.184.252.89:8080/"

    val apiService: APIService
        get() = RetrofitClient.getClient(BASE_URL)!!.create(
            APIService::class.java
        )
}