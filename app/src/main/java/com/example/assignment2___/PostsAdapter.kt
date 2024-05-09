package com.example.assignment2___

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

// Assuming Post is defined somewhere in your project
data class Post(val id: Int, val imageUrl: String, val caption: String, val likes: Int, val comments: Int)

class PostsAdapter(private val posts: List<Post>, private val listener: PostInteractionListener) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    interface PostInteractionListener {
        fun onLikeClicked(postId: Int)
        fun onCommentClicked(postId: Int)
    }

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.postImageView)
        val captionTextView: TextView = view.findViewById(R.id.captionTextView)
        val likeButton: Button = view.findViewById(R.id.likeButton)
        val commentButton: Button = view.findViewById(R.id.commentButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.captionTextView.text = post.caption
        // Load image with an image loader library
        holder.likeButton.setOnClickListener { listener.onLikeClicked(post.id) }
        holder.commentButton.setOnClickListener { listener.onCommentClicked(post.id) }
    }

    override fun getItemCount(): Int = posts.size
}
