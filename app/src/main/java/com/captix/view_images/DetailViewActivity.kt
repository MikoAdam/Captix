package com.captix.view_images

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.captix.R
import com.captix.http_requests.comment_request.CommentResponse
import com.captix.http_requests.comment_request.CommentUpload
import com.captix.http_requests.vote.Vote
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
        val requestUrlForComments = "/posts/${postId}/comments"
        val requestUrlForVotes = "/posts/${postId}/vote"

        val mAPIService: APIService?
        mAPIService = ApiUtils.apiService

        var upVote: Int = intent.getStringExtra("upVote").toInt()
        var downVote: Int = intent.getStringExtra("downVote").toInt()

        textViewUpDownVote.text = "upvote: $upVote downvote: $downVote"

        mAPIService.getComments(token, requestUrlForComments).enqueue(object :
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

        buttonSendComment.setOnClickListener {
            if (editTextComment.text.toString() != "" && (radioButtonUpvote.isChecked || radioButtonDownvote.isChecked)) {

                val comment = CommentUpload(editTextComment.text.toString())
                mAPIService.sendComment(token, requestUrlForComments, comment)
                    .enqueue(object :
                        Callback<CommentResponse> {

                        override fun onResponse(
                            call: Call<CommentResponse>,
                            response: Response<CommentResponse>
                        ) {
                            val urlResponse = response.body()

                            if (urlResponse != null) {

                                comments.add(
                                    Comment(
                                        _id = urlResponse._id,
                                        username = urlResponse.username,
                                        content = urlResponse.content
                                    )
                                )
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

                        override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                            FancyToast.makeText(
                                applicationContext,
                                "Server error",
                                FancyToast.LENGTH_SHORT,
                                FancyToast.ERROR,
                                false
                            ).show()
                        }
                    })


                val voteContent = if (radioButtonUpvote.isChecked) {
                    "up"
                } else {
                    "down"
                }

                val vote = Vote(voteContent)

                mAPIService.sendVote(token, requestUrlForVotes, vote).enqueue(object :
                    Callback<Vote> {

                    override fun onResponse(call: Call<Vote>, response: Response<Vote>) {

                        val urlResponse = response.body()

                        if (urlResponse != null) {


                            if (urlResponse.vote == "upvote") {
                                upVote++
                            } else {
                                downVote++
                            }

                            textViewUpDownVote.text = "upvote: $upVote downvote: $downVote"
                            
                        }


                    }

                    override fun onFailure(call: Call<Vote>, t: Throwable) {
                        FancyToast.makeText(
                            applicationContext,
                            "Server error",
                            FancyToast.LENGTH_SHORT,
                            FancyToast.ERROR,
                            false
                        ).show()
                    }
                })


                editTextComment.text.clear()
            } else {
                FancyToast.makeText(
                    applicationContext,
                    "First write something and vote",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }
        }

    }

    fun showComments() {
        var commentsTry = ""
        for (i in comments) {
            if (i != null) {
                commentsTry += "- " + i.username + ": " + i.content.toString() + "\n"
            }
        }

        TextViewDisplayComments.text = commentsTry
    }
}
