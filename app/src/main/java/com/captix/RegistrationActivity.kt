package com.captix

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.captix.http_requests.registration.Registration
import com.captix.retrofit.APIService
import com.captix.retrofit.ApiUtils
import kotlinx.android.synthetic.main.activity_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val mAPIService: APIService?
        mAPIService = ApiUtils.apiService

        btnRegistration.setOnClickListener {
            val email: String = emailRegistrationTextView.text.toString()
            val userName: String = userNameRegistrationEditText.text.toString()
            val password: String = passwordRegistrationEditText.text.toString()

            val registration =
                Registration(email, userName, password)

            mAPIService.registrationPost(registration).enqueue(object :
                Callback<Registration> {

                override fun onResponse(
                    call: Call<Registration>,
                    response: Response<Registration>
                ) {

                    if (response.isSuccessful) {
                        val text = "Registration success"
                        val duration = Toast.LENGTH_LONG

                        val toast = Toast.makeText(applicationContext, text, duration)
                        toast.show()
                    }
                }

                override fun onFailure(call: Call<Registration>, t: Throwable) {
                    val text = "Registration fail try again"
                    val duration = Toast.LENGTH_LONG

                    val toast = Toast.makeText(applicationContext, text, duration)
                    toast.show()
                }
            })
        }

        btnNavigateLogIn.setOnClickListener {
/*            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)*/

            val intent = Intent(this, UserPhotosActivity::class.java)
            startActivity(intent)
        }
    }
}