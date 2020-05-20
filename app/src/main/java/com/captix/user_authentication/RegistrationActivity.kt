package com.captix.user_authentication

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.captix.R
import com.captix.http_requests.registration.Registration
import com.captix.retrofit.APIService
import com.captix.retrofit.ApiUtils
import com.flaviofaria.kenburnsview.KenBurnsView
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.activity_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationActivity : AppCompatActivity() {

    companion object {
        val resources = listOf(
            R.drawable.background1, R.drawable.background2,
            R.drawable.background3, R.drawable.background4,
            R.drawable.background5, R.drawable.background6
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val mAPIService: APIService?
        mAPIService = ApiUtils.apiService

        btnRegistration.setOnClickListener {
            when {
                emailRegistrationTextView.text.toString().isEmpty() -> {
                    emailRegistrationTextView.requestFocus()
                    emailRegistrationTextView.error = "Please enter your email address"
                }
                userNameRegistrationEditText.text.toString().isEmpty() -> {
                    userNameRegistrationEditText.requestFocus()
                    userNameRegistrationEditText.error = "Please enter your user name"
                }
                passwordRegistrationEditText.text.toString().isEmpty() -> {
                    passwordRegistrationEditText.requestFocus()
                    passwordRegistrationEditText.error = "Please enter your password"
                }
                else -> {
                    if (emailRegistrationTextView.text.toString().isValidEmail()) {
                        val registration = createRegistrationData()

                        sendRegistrationRequest(mAPIService, registration)
                    } else {
                        emailRegistrationTextView.requestFocus()
                        emailRegistrationTextView.error = "Please enter valid email address"
                    }
                }
            }
        }

        btnNavigateLogIn.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val kenBurnsView: KenBurnsView = findViewById(R.id.kenBurnsImageRegistration)
        kenBurnsView.setImageResource(RegistrationActivity.resources.random())
        kenBurnsView.restart()
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
                    "Server error, try again later",
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

    private fun CharSequence.isValidEmail() =
        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

}