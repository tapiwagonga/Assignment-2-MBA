package com.example.assignment2___

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class PostsAdapter(
    private val context: Context,
    private var posts: MutableList<Post>, // Changed to var to allow reassignment
    private val onLike: (Post) -> Unit,
    private val onComment: (Post) -> Unit,
    private val onEdit: (Post) -> Unit,
    private val onDelete: (Post) -> Unit
) : RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contentTextView: TextView = view.findViewById(R.id.contentTextView)
        val postImageView: ImageView = view.findViewById(R.id.postImageView)
        val likeIcon: ImageView = view.findViewById(R.id.likeIcon)
        val likesCountTextView: TextView = view.findViewById(R.id.likesCountTextView)
        val commentIcon: ImageView = view.findViewById(R.id.commentIcon)
        val commentsCountTextView: TextView = view.findViewById(R.id.commentsCountTextView)
        val editIcon: ImageView = view.findViewById(R.id.editIcon)
        val deleteIcon: ImageView = view.findViewById(R.id.deleteIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.contentTextView.text = post.content
        holder.likesCountTextView.text = post.likesCount.toString()
        holder.commentsCountTextView.text = post.commentsCount.toString()

        // Load image using Picasso
        Picasso.get().load(post.imageUri).into(holder.postImageView)

        holder.likeIcon.setOnClickListener { onLike(post) }
        holder.commentIcon.setOnClickListener { onComment(post) }
        holder.editIcon.setOnClickListener { onEdit(post) }
        holder.deleteIcon.setOnClickListener { onDelete(post) }
    }

    override fun getItemCount() = posts.size

    fun updatePosts(newPosts: List<Post>) {
        posts = newPosts.toMutableList()
        notifyDataSetChanged()
    }
}
