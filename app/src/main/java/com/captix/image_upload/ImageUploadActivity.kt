package com.captix.image_upload

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.captix.R
import com.github.dhaval2404.imagepicker.ImagePicker
import com.livinglifetechway.quickpermissions.annotations.WithPermissions
import kotlinx.android.synthetic.main.activity_image_upload.*
import okhttp3.ResponseBody

class ImageUploadActivity : AppCompatActivity() {

    companion object {
        lateinit var imageURI: Uri
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_upload)

        btnCapture.setOnClickListener {
            ImagePicker.with(this)
                .start()
        }

        btnUpload.setOnClickListener {
            val galleryInteractor = GalleryInteractor()

            galleryInteractor.uploadImage(
                fileUri = imageURI,
                onSuccess = this::uploadSuccess,
                onError = this::uploadError
            )
        }
    }

    @WithPermissions(
        permissions = [Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE]
    )


    private fun uploadSuccess(responseBody: ResponseBody) {
        Toast.makeText(this, "Successfully uploaded!", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun uploadError(e: Throwable) {
        Toast.makeText(this, "Error during uploading photo!", Toast.LENGTH_SHORT).show()
        e.printStackTrace()
    }

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