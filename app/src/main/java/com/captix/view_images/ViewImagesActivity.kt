package com.captix.view_images

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.captix.R
import com.captix.http_requests.image_request.ImageResponse
import com.captix.model.Post
import com.captix.retrofit.APIService
import com.captix.retrofit.ApiUtils
import com.captix.user_authentication.LogInActivity
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.activity_user_photos.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewImagesActivity : AppCompatActivity() {

    //the images asked from the server will be here
    private val postsFromServer: MutableList<Post> = mutableListOf()
    private lateinit var postsRecyclerViewAdapter: postsRecyclerViewAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager


    private fun setupRecyclerView() {
        postsRecyclerViewAdapter = postsRecyclerViewAdapter()
        postsRecyclerViewAdapter.setPosts(postsFromServer)
        recyclerView.adapter = postsRecyclerViewAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_photos)

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        //api service for http request
        val mAPIService: APIService?
        mAPIService = ApiUtils.apiService

        mAPIService.getPhotosURL(/*token*/).enqueue(object :
            Callback<List<ImageResponse>> {

            override fun onResponse(
                call: Call<List<ImageResponse>>,
                response: Response<List<ImageResponse>>
            ) {
                val urlResponse = response.body()

                if (urlResponse != null) {
                    for (i in urlResponse) {
                        postsFromServer.add(
                            Post(
                                username = i.username,
                                imageUrl = i.imageUrl,
                                caption = i.caption,
                                rating = i.rating,
                                dateOfCreation = i.dateOfCreation,
                                categoryName = i.categoryName
                            )
                        )
                    }
                }
                setupRecyclerView()
            }

            override fun onFailure(call: Call<List<ImageResponse>>, t: Throwable) {

                val error = t.cause.toString()
                Log.d("image download", error)

                FancyToast.makeText(
                    applicationContext,
                    "Server error",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }
        })

/*        val token = LogInActivity.token
        val url = "https://moodle.htwchur.ch/pluginfile.php/124614/mod_page/content/4/example.jpg"
        val glideUrl = GlideUrl(url) { mapOf(Pair("Authorization", "Bearer $token")) }

        Glide.with(this)
            .load(glideUrl)
            .into(UserImagesImageView)*/
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Log out")
        builder.setMessage("Do you want to log out?")

        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            startActivity(Intent(this@ViewImagesActivity, LogInActivity::class.java))
            finish()
        }

        builder.setNegativeButton(android.R.string.no) { _, _ -> }
        builder.show()
    }


/*    private fun getPicWithGlide(position: Int) {
        val url = postsFromServer[position].imageUrl
        Glide.with(this)
            .load(url)
            .into(UserImagesImageView)
    }*/
}