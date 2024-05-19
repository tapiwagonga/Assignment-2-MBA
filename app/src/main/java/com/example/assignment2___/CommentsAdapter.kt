package com.example.assignment2___

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommentsAdapter(
    private val comments: MutableList<Comment>,
    private val dbHelper: DatabaseHelper,
    private val onLike: (Int) -> Unit,
    private val onDislike: (Int) -> Unit
) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contentTextView: TextView = view.findViewById(R.id.contentTextView)
        val authorTextView: TextView = view.findViewById(R.id.authorTextView)
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
        val likeImageView: ImageView = view.findViewById(R.id.likeIcon)
        val dislikeImageView: ImageView = view.findViewById(R.id.dislikeIcon)
        val likeCountTextView: TextView = view.findViewById(R.id.likeCountTextView)
        val dislikeCountTextView: TextView = view.findViewById(R.id.dislikeCountTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]
        holder.contentTextView.text = comment.content
        holder.authorTextView.text = dbHelper.getUserNameById(comment.authorId) ?: "Unknown"
        holder.dateTextView.text = formatDate(comment.dateCreated)
        holder.likeCountTextView.text = comment.likesCount.toString()
        holder.dislikeCountTextView.text = comment.dislikesCount.toString()

        holder.likeImageView.setOnClickListener {
            onLike(comment.id)
        }

        holder.dislikeImageView.setOnClickListener {
            onDislike(comment.id)
        }
    }

    override fun getItemCount() = comments.size

    private fun formatDate(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }

    fun addComment(comment: Comment) {
        comments.add(comment)
        notifyItemInserted(comments.size - 1)
    }

    fun updateCommentLikeStatus(commentId: Int, isLike: Boolean) {
        val comment = comments.find { it.id == commentId }
        comment?.let {
            if (isLike) {
                it.likesCount += 1
            } else {
                it.dislikesCount += 1
            }
            notifyItemChanged(comments.indexOf(it))
        }
    }
}
