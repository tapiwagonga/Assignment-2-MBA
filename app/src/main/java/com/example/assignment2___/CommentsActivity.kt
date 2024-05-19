package com.example.assignment2___

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CommentsActivity : AppCompatActivity() {
    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var addCommentEditText: EditText
    private lateinit var addCommentButton: Button
    private lateinit var backButton: Button
    private var postId: Int = -1
    private var userId: Int = 1 // Replace with actual user ID logic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        commentsRecyclerView = findViewById(R.id.commentsRecyclerView)
        commentsRecyclerView.layoutManager = LinearLayoutManager(this)
        addCommentEditText = findViewById(R.id.addCommentEditText)
        addCommentButton = findViewById(R.id.addCommentButton)
        backButton = findViewById(R.id.backButton)
        dbHelper = DatabaseHelper(this)

        postId = intent.getIntExtra("POST_ID", -1)
        if (postId != -1) {
            loadComments(postId)
        } else {
            // Handle error
        }

        addCommentButton.setOnClickListener {
            val commentContent = addCommentEditText.text.toString().trim()
            if (commentContent.isNotEmpty()) {
                val newComment = dbHelper.addComment(this, postId, userId, commentContent)
                if (newComment != null) {
                    commentsAdapter.addComment(newComment)
                    NotificationUtils.sendNotification(this, "New Comment", "Your comment has been added.")
                    addCommentEditText.text.clear() // Clear the input field
                }
            }
        }

        backButton.setOnClickListener {
            finish() // Go back to the previous activity
        }
    }

    private fun loadComments(postId: Int) {
        val comments = dbHelper.getCommentsByPostId(postId).toMutableList()
        commentsAdapter = CommentsAdapter(comments, dbHelper,
            onLike = { commentId -> addLikeToComment(commentId) },
            onDislike = { commentId -> addDislikeToComment(commentId) }
        )
        commentsRecyclerView.adapter = commentsAdapter
    }

    private fun addLikeToComment(commentId: Int) {
        dbHelper.addLikeToComment(commentId)
        commentsAdapter.updateCommentLikeStatus(commentId, true)
    }

    private fun addDislikeToComment(commentId: Int) {
        dbHelper.addDislikeToComment(commentId)
        commentsAdapter.updateCommentLikeStatus(commentId, false)
    }
}
