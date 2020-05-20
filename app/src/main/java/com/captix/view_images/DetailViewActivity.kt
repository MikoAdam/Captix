package com.captix.view_images

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.captix.R
import com.captix.http_requests.comment_request.CommentResponse
import com.captix.http_requests.comment_request.CommentUpload
import com.captix.model.Comment
import com.captix.retrofit.APIService
import com.captix.retrofit.ApiUtils
import com.captix.user_authentication.LogInActivity.Companion.token
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.activity_detail_view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewActivity : AppCompatActivity() {

    private val comments: MutableList<Comment?> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_view)

        val postId = intent.getStringExtra("post_id")
        val requestUrl = "/posts/${postId}/comments"

        val mAPIService: APIService?
        mAPIService = ApiUtils.apiService

        mAPIService.getComments(token, requestUrl).enqueue(object :
            Callback<List<CommentResponse>> {

            override fun onResponse(
                call: Call<List<CommentResponse>>,
                response: Response<List<CommentResponse>>
            ) {
                val urlResponse = response.body()

                if (urlResponse != null) {
                    for (i in urlResponse) {
                        comments.add(
                            Comment(
                                _id = i._id,
                                username = i.username,
                                content = i.content
                            )
                        )
                    }
                }

                showComments()
            }

            override fun onFailure(call: Call<List<CommentResponse>>, t: Throwable) {
                val error = t.cause.toString()
                Log.d("comment download", error)

                FancyToast.makeText(
                    applicationContext,
                    "Server error",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }

        })

        if (editTextComment.toString() != "") {
            buttonSendComment.setOnClickListener {

                val comment = CommentUpload(editTextComment.text.toString())

                mAPIService.sendComment(token, requestUrl, comment)
                    .enqueue(object :
                        Callback<List<CommentResponse>> {

                        override fun onResponse(
                            call: Call<List<CommentResponse>>,
                            response: Response<List<CommentResponse>>
                        ) {

                            val urlResponse = response.body()

                            if (urlResponse != null) {
                                for (i in urlResponse) {

                                    var commentIsNew = true

                                    // avoid adding the same comment again
                                    for (j in comments) {
                                        if (j != null) {
                                            if (j._id == i._id) {
                                                commentIsNew = false
                                            }
                                        }
                                    }

                                    if (commentIsNew) {
                                        comments.add(
                                            Comment(
                                                _id = i._id,
                                                username = i.username,
                                                content = i.content
                                            )
                                        )
                                    }
                                }
                            }

                            showComments()

                            FancyToast.makeText(
                                applicationContext,
                                "Comment uploaded",
                                FancyToast.LENGTH_SHORT,
                                FancyToast.SUCCESS,
                                false
                            ).show()
                        }

                        override fun onFailure(call: Call<List<CommentResponse>>, t: Throwable) {
                            val error = t.cause.toString()
                            Log.d("comment download", error)

/*                            FancyToast.makeText(
                                applicationContext,
                                "Server error",
                                FancyToast.LENGTH_SHORT,
                                FancyToast.ERROR,
                                false
                            ).show()*/

                            showComments()
                        }
                    })
                showComments()
                editTextComment.text.clear()
            }
        }
    }

    fun showComments() {
        var commentsTry = ""
        for (i in comments) {
            if (i != null) {
                commentsTry += "- " + i.username + ": " + i.content.toString() +"\n"
            }
        }

        TextViewDisplayComments.text = commentsTry
    }
}