package com.example.assignment2___

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

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
        val likeIcon: ImageView = view.findViewById(R.id.likeIcon)
        val dislikeIcon: ImageView = view.findViewById(R.id.dislikeIcon)
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
        holder.authorTextView.text = comment.authorName
        holder.dateTextView.text = formatDate(comment.dateCreated)
        holder.likeCountTextView.text = comment.likesCount.toString()
        holder.dislikeCountTextView.text = comment.dislikesCount.toString()

        holder.likeIcon.setOnClickListener {
            onLike(comment.id)
            notifyItemChanged(position)
        }

        holder.dislikeIcon.setOnClickListener {
            onDislike(comment.id)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = comments.size

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
