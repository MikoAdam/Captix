package com.captix.user_authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.captix.R
import com.captix.ViewImagesActivity
import com.captix.http_requests.login.LoginRequest
import com.captix.http_requests.login.LoginResponse
import com.captix.retrofit.APIService
import com.captix.retrofit.ApiUtils
import kotlinx.android.synthetic.main.activity_log_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInActivity : AppCompatActivity() {

    companion object {
        lateinit var token: String
            private set
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val mAPIService: APIService?
        mAPIService = ApiUtils.apiService

        btnLogIn.setOnClickListener {

            val userName: String = userNameLogIneditText.text.toString()
            val password: String = passwordLogInEditText.text.toString()

            val logInRequest = LoginRequest(userName, password)

            mAPIService.login(logInRequest)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        val text = "Log in fail try again"
                        val duration = Toast.LENGTH_LONG

                        val toast = Toast.makeText(applicationContext, text, duration)
                        toast.show()
                    }

                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        val loginResponse = response.body()

                        if (loginResponse != null) {
                            token = loginResponse.jwt

                            val intent = Intent(this@LogInActivity, ViewImagesActivity::class.java)
                            startActivity(intent)
                        }
                    }
                })
        }
    }
}
