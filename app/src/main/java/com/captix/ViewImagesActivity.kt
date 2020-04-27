package com.captix

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.captix.http_requests.image_request.ImageResponse
import com.captix.retrofit.APIService
import com.captix.retrofit.ApiUtils
import kotlinx.android.synthetic.main.activity_user_photos.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewImagesActivity : AppCompatActivity() {

    val urls: MutableList<ImageResponse> = mutableListOf<ImageResponse>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_photos)

        val mAPIService: APIService?
        mAPIService = ApiUtils.apiService


        var picPosition = 0

        mAPIService.getPhotosURL().enqueue(object :
            Callback<List<ImageResponse>> {

            override fun onResponse(
                call: Call<List<ImageResponse>>,
                response: Response<List<ImageResponse>>
            ) {
                val URLResponse = response.body()

                if (URLResponse != null) {
                    for (i in URLResponse) {
                        urls.add(i)
                    }
                    PicNumberTextView.text = "${urls.size}/0 "
                }

                getPicWithGlide(picPosition)
            }

            override fun onFailure(call: Call<List<ImageResponse>>, t: Throwable) {
                val text = "Image load fail try again"
                val duration = Toast.LENGTH_LONG

                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
            }
        })

        btnImage1.setOnClickListener {
            if (picPosition > 0) {
                picPosition--
                getPicWithGlide(picPosition)
                PicNumberTextView.text = "${urls.size}/${picPosition + 1} "
            } else {
                val text = "There are no more pics"
                val duration = Toast.LENGTH_LONG

                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
            }
        }

        btnImage2.setOnClickListener {
            if (picPosition < urls.size - 1) {
                picPosition++
                getPicWithGlide(picPosition)
                PicNumberTextView.text = "${urls.size}/${picPosition + 1} "
            } else {
                val text = "There are no more pics"
                val duration = Toast.LENGTH_LONG

                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
            }
        }


/*        val token = LogInActivity.token
        val url = "https://moodle.htwchur.ch/pluginfile.php/124614/mod_page/content/4/example.jpg"
        val glideUrl = GlideUrl(url) { mapOf(Pair("Authorization", "Bearer $token")) }

        Glide.with(this)
            .load(glideUrl)
            .into(UserImagesImageView)*/
    }

    private fun getPicWithGlide(position: Int) {
        val url = urls[position].url
        Glide.with(this)
            .load(url)
            .into(UserImagesImageView)
    }
}
