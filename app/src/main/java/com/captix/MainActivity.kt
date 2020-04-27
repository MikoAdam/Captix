package com.captix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.captix.image_upload.ImageUploadActivity
import com.captix.user_authentication.LogInActivity
import com.captix.user_authentication.RegistrationActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerbtn.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        loginbtn.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }

        imageViewbtn.setOnClickListener {
            val intent = Intent(this, ViewImagesActivity::class.java)
            startActivity(intent)
        }

        uploadImagebtn.setOnClickListener {
            val intent = Intent(this, ImageUploadActivity::class.java)
            startActivity(intent)
        }
    }
}
