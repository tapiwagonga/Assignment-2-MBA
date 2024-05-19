package com.example.assignment2___

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ManagePostsActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_posts)

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.managePostsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadPosts()
    }

    private fun loadPosts() {
        val posts = dbHelper.getPostsByUserId(1) // Assuming user id 1
        adapter = PostsAdapter(
            posts,
            onLike = { postId ->
                // No action needed for managing posts
            },
            onComment = { postId ->
                // No action needed for managing posts
            },
            onEdit = { post ->
                val intent = Intent(this, EditPostActivity::class.java)
                intent.putExtra("POST_ID", post.id)
                startActivity(intent)
            },
            onDelete = { postId ->
                dbHelper.deletePost(postId)
                loadPosts() // Reload posts after deletion
                Toast.makeText(this, "Post deleted", Toast.LENGTH_SHORT).show()
            }
        )
        recyclerView.adapter = adapter
    }
}
