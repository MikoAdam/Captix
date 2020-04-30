package com.captix.view_images

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.captix.R
import com.captix.http_requests.image_request.ImageResponse
import com.captix.image_upload.ImageUploadActivity
import com.captix.model.Post
import com.captix.retrofit.APIService
import com.captix.retrofit.ApiUtils
import com.captix.user_authentication.LogInActivity
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.activity_view_images.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewImagesActivity : AppCompatActivity() {

    private val postsFromServer: MutableList<Post> = mutableListOf()
    private lateinit var postsRecyclerViewAdapter: PostsRecyclerViewAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private fun setupRecyclerView() {
        postsRecyclerViewAdapter = PostsRecyclerViewAdapter()
        postsRecyclerViewAdapter.setPosts(postsFromServer)
        recyclerView.adapter = postsRecyclerViewAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_images)

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        val fab: View = findViewById(R.id.floatingActionButtonUploadImage)
        fab.setOnClickListener {
            val intent = Intent(this@ViewImagesActivity, ImageUploadActivity::class.java)
            startActivity(intent)
        }

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
}