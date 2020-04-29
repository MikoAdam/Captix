package com.captix.user_authentication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.captix.R
import com.captix.http_requests.login.LoginRequest
import com.captix.http_requests.login.LoginResponse
import com.captix.retrofit.APIService
import com.captix.retrofit.ApiUtils
import com.captix.view_images.ViewImagesActivity
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.activity_log_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInActivity : AppCompatActivity() {

    companion object {
        lateinit var token: String
            private set
        var userName: String = "Guest"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val mAPIService: APIService?
        mAPIService = ApiUtils.apiService

        btnLogIn.setOnClickListener {
            val logInRequest = getLogInData()
            sendLogInRequest(mAPIService, logInRequest)
        }
    }

    private fun sendLogInRequest(mAPIService: APIService, logInRequest: LoginRequest) {
        mAPIService.login(logInRequest)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    val loginResponse = response.body()

                    if (loginResponse != null) {
                        token = loginResponse.jwt

                        userName = userNameLogIneditText.text.toString()

                        val intent = Intent(this@LogInActivity, ViewImagesActivity::class.java)
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    FancyToast.makeText(
                        applicationContext,
                        "Log in error, try again",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                    ).show()
                }
            })
    }

    private fun getLogInData(): LoginRequest {
        val userName: String = userNameLogIneditText.text.toString()
        val password: String = passwordLogInEditText.text.toString()

        return LoginRequest(userName, password)
    }
}
