package com.captix.view_images

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.captix.R
import com.captix.model.Post
import kotlinx.android.synthetic.main.post_item.view.*

class PostsRecyclerViewAdapter : RecyclerView.Adapter<PostsRecyclerViewAdapter.PostViewHolder>() {

    private val postList = mutableListOf<Post>()
    var itemClickListener: PostItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, null)
        return PostViewHolder(view)
    }


    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]

        holder.imageTitle.text = post.caption
        holder.userName.text = post.username

        holder.rating.text = "upvote: ${post.upVote}  downvote: ${post.downVote}"

        Glide.with(holder.image.context)
            .load(post.imageUrl)
            .into(holder.image)

        holder.category.text = post.categoryName
        //holder.description.text = post.description

        holder.post = post
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    fun setPosts(posts: List<Post>) {
        postList.clear()
        postList += posts
        notifyDataSetChanged()
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageTitle: TextView = itemView.textViewImageTitle
        val userName: TextView = itemView.textViewOwner
        val image: ImageView = itemView.imageViewImage
        val category: TextView = itemView.textViewCategory
        val rating: TextView = itemView.textViewRating

        var post: Post? = null

        init {
            itemView.setOnClickListener {
                post?.let {
                    itemClickListener?.onItemClick(it)
                }
            }
        }
    }


    interface PostItemClickListener {
        fun onItemClick(post: Post)
    }
}