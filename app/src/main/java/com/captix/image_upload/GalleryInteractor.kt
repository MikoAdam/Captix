package com.captix.image_upload

import android.net.Uri
import android.os.Handler
import com.captix.retrofit.APIService
import com.captix.retrofit.APIService.Companion.MULTIPART_FORM_DATA
import com.captix.retrofit.APIService.Companion.PHOTO_MULTIPART_KEY_IMG
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class GalleryInteractor {

    private val galleryApi: APIService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(APIService.ENDPOINT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        this.galleryApi = retrofit.create(APIService::class.java)
    }

    private fun <T> runCallOnBackgroundThread(
        call: Call<T>,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val handler = Handler()
        Thread {
            try {
                val response = call.execute().body()!!
                handler.post { onSuccess(response) }

            } catch (e: Exception) {
                e.printStackTrace()
                handler.post { onError(e) }
            }
        }.start()
    }

    fun uploadImage(
        fileUri: Uri,
        onSuccess: (ResponseBody) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val file = File(fileUri.path)
        val requestFile = file.asRequestBody(MULTIPART_FORM_DATA.toMediaTypeOrNull())
        val body =
            MultipartBody.Part.createFormData(PHOTO_MULTIPART_KEY_IMG, file.name, requestFile)

        val uploadImageRequest = galleryApi.uploadImage(body)
        runCallOnBackgroundThread(uploadImageRequest, onSuccess, onError)
    }
}