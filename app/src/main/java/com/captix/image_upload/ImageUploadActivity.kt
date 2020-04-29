package com.captix.image_upload

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.captix.R
import com.captix.http_requests.image_upload.ImageUploadResponse
import com.captix.retrofit.APIService
import com.captix.retrofit.ApiUtils
import com.github.dhaval2404.imagepicker.ImagePicker
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.activity_image_upload.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ImageUploadActivity : AppCompatActivity() {

    private lateinit var imageURI: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_upload)

        val mAPIService: APIService?
        mAPIService = ApiUtils.apiService

        btnCapture.setOnClickListener {
            ImagePicker.with(this)
                .start()
        }

        btnUpload.setOnClickListener {
            if (this::imageURI.isInitialized) {
                uploadImage(mAPIService)
            } else {
                FancyToast.makeText(
                    applicationContext,
                    "First choose an image",
                    Toast.LENGTH_SHORT,
                    FancyToast.WARNING,
                    false
                ).show()
            }
        }
    }

    private fun uploadImage(mAPIService: APIService) {
        val file = File(imageURI.path.toString())
        val requestFile = file.asRequestBody(APIService.MULTIPART_FORM_DATA.toMediaTypeOrNull())
        val body =
            MultipartBody.Part.createFormData(
                APIService.PHOTO_MULTIPART_KEY_IMG,
                file.name,
                requestFile
            )

        mAPIService.uploadImage(body)
            .enqueue(object : Callback<ImageUploadResponse> {
                override fun onResponse(
                    call: Call<ImageUploadResponse>,
                    response: Response<ImageUploadResponse>
                ) {
                    var s = "error on success"
                    val imageUploadResponse = response.body()

                    if (imageUploadResponse != null) {
                        s = imageUploadResponse.ImageUrl
                    }

                    FancyToast.makeText(
                        applicationContext,
                        s,
                        Toast.LENGTH_SHORT,
                        FancyToast.SUCCESS,
                        false
                    ).show()
                }

                override fun onFailure(call: Call<ImageUploadResponse>, t: Throwable) {
                    FancyToast.makeText(
                        applicationContext,
                        "Upload fail try again",
                        Toast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                    ).show()
                }
            })
    }

    //after choosing a image this set the uri and set on imageView
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                val fileUri = data?.data
                if (fileUri != null)
                    imageURI = fileUri
                ivImage.setImageURI(fileUri)
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}