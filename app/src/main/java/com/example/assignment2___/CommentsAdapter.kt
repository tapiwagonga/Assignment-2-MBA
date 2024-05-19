package com.example.assignment2___

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class CommentsAdapter(
    val comments: MutableList<Comment>, // Make this public
    private val onLike: (Int) -> Unit,
    private val onDislike: (Int) -> Unit
) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contentTextView: TextView = view.findViewById(R.id.contentTextView)
        val authorTextView: TextView = view.findViewById(R.id.authorTextView)
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
        val likeImageView: ImageView = view.findViewById(R.id.likeImageView)
        val dislikeImageView: ImageView = view.findViewById(R.id.dislikeImageView)
        val likesCountTextView: TextView = view.findViewById(R.id.likesCountTextView)
        val dislikesCountTextView: TextView = view.findViewById(R.id.dislikesCountTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]
        holder.contentTextView.text = comment.content
        holder.authorTextView.text = comment.authorName
        holder.dateTextView.text = formatDate(comment.dateCreated)
        holder.likesCountTextView.text = comment.likesCount.toString()
        holder.dislikesCountTextView.text = comment.dislikesCount.toString()

        holder.likeImageView.setOnClickListener {
            onLike(comment.id)
            comment.likesCount += 1
            notifyItemChanged(position)
        }

        holder.dislikeImageView.setOnClickListener {
            onDislike(comment.id)
            comment.dislikesCount += 1
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = comments.size

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
