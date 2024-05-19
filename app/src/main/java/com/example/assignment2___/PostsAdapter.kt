package com.example.assignment2___

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostsAdapter(
    private var posts: List<Post>,
    private val onLike: (Int) -> Unit,
    private val onComment: (Int) -> Unit,
    private val onEdit: (Post) -> Unit,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    inner class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contentTextView: TextView = view.findViewById(R.id.contentTextView)
        val likeIcon: ImageView = view.findViewById(R.id.likeIcon)
        val likesCountTextView: TextView = view.findViewById(R.id.likesCountTextView)
        val commentIcon: ImageView = view.findViewById(R.id.commentIcon)
        val commentsCountTextView: TextView = view.findViewById(R.id.commentsCountTextView)
        val editIcon: ImageView = view.findViewById(R.id.editIcon)
        val deleteIcon: ImageView = view.findViewById(R.id.deleteIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.contentTextView.text = post.content
        holder.likesCountTextView.text = post.likesCount.toString()
        holder.commentsCountTextView.text = post.commentsCount.toString()

        holder.likeIcon.setOnClickListener {
            onLike(post.id)
        }

        holder.commentIcon.setOnClickListener {
            onComment(post.id)
        }

        holder.editIcon.setOnClickListener {
            showEditDialog(holder.itemView.context, post)
        }

        holder.deleteIcon.setOnClickListener {
            onDelete(post.id)
        }
    }

    override fun getItemCount() = posts.size

    fun updatePosts(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    private fun showEditDialog(context: Context, post: Post) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Post")

        val input = EditText(context)
        input.setText(post.content)
        builder.setView(input)

        builder.setPositiveButton("Save") { dialog, _ ->
            val newCaption = input.text.toString()
            if (newCaption.isNotBlank()) {
                post.content = newCaption
                onEdit(post)
                notifyItemChanged(posts.indexOf(post))
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }
}
