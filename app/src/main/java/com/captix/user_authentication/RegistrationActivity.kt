package com.captix.user_authentication

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.captix.R
import com.captix.http_requests.registration.Registration
import com.captix.retrofit.APIService
import com.captix.retrofit.ApiUtils
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.activity_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationActivity : AppCompatActivity() {

    private fun CharSequence.isValidEmail() =
        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val mAPIService: APIService?
        mAPIService = ApiUtils.apiService

        btnRegistration.setOnClickListener {
            if (emailRegistrationTextView.text.toString().isValidEmail()) {
                val registration =
                    createRegistrationData()

                sendRegistrationRequest(mAPIService, registration)
            }else{
                FancyToast.makeText(
                    applicationContext,
                    "Email address is invalid",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }
        }

        btnNavigateLogIn.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun sendRegistrationRequest(mAPIService: APIService, registration: Registration) {
        mAPIService.registrationPost(registration).enqueue(object :
            Callback<Registration> {

            override fun onResponse(
                call: Call<Registration>,
                response: Response<Registration>
            ) {
                if (response.isSuccessful) {
                    FancyToast.makeText(
                        applicationContext,
                        "Registration success",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.SUCCESS,
                        false
                    ).show()
                }
            }

            override fun onFailure(call: Call<Registration>, t: Throwable) {
                FancyToast.makeText(
                    applicationContext,
                    "Registration fail try again",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }
        })
    }

    private fun createRegistrationData(): Registration {
        val email: String = emailRegistrationTextView.text.toString()
        val userName: String = userNameRegistrationEditText.text.toString()
        val password: String = passwordRegistrationEditText.text.toString()

        return Registration(email, userName, password)
    }
}