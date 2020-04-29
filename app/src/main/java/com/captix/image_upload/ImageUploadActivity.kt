package com.captix.image_upload

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.captix.R
import com.captix.http_requests.image_upload.ImageUploadResponse
import com.captix.http_requests.image_upload.ImageUploadSendBack
import com.captix.retrofit.APIService
import com.captix.retrofit.ApiUtils
import com.captix.user_authentication.LogInActivity.Companion.userName
import com.captix.view_images.ViewImagesActivity
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

    private fun uploadImageAnswer(mAPIService: APIService, uploadAnswer: ImageUploadSendBack) {
        mAPIService.uploadImageAnswer(uploadAnswer)
            .enqueue(object : Callback<ImageUploadSendBack> {
                override fun onResponse(
                    call: Call<ImageUploadSendBack>,
                    response: Response<ImageUploadSendBack>
                ) {
                    FancyToast.makeText(
                        applicationContext,
                        "Successfully uploaded",
                        Toast.LENGTH_SHORT,
                        FancyToast.SUCCESS,
                        false
                    ).show()

                    val intent = Intent(this@ImageUploadActivity, ViewImagesActivity::class.java)
                    startActivity(intent)
                }

                override fun onFailure(call: Call<ImageUploadSendBack>, t: Throwable) {
                    val error = t.cause.toString()
                    Log.d("image upload", error)

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
                    val uploadAnswer: ImageUploadSendBack
                    val url = response.body()?.imageUrl
                    if (url != null) {
                        uploadAnswer = ImageUploadSendBack(
                            userName,
                            url,
                            editTextTitle.text.toString(),
                            textInputEditTextDescription.text.toString(),
                            editTextTitle.text.toString()
                        )
                        uploadImageAnswer(mAPIService, uploadAnswer)
                    }
                }

                override fun onFailure(call: Call<ImageUploadResponse>, t: Throwable) {
                    val error = t.cause.toString()
                    Log.d("image upload", error)

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
                imageViewImagePreview.setImageURI(fileUri)
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