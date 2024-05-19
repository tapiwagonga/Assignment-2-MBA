package com.example.assignment2___

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ManagePostsActivity : AppCompatActivity() {

    private lateinit var postsRecyclerView: RecyclerView
    private lateinit var postsAdapter: PostsAdapter
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_posts)

        postsRecyclerView = findViewById(R.id.postsRecyclerView)
        postsRecyclerView.layoutManager = LinearLayoutManager(this)
        dbHelper = DatabaseHelper(this)

        loadPosts()
    }

    private fun loadPosts() {
        val posts = dbHelper.getAllPosts()
        postsAdapter = PostsAdapter(
            this,
            posts.toMutableList(),
            onLike = { post -> addLike(post) },
            onComment = { post -> openComments(post.id) },
            onEdit = { post -> editPost(post.id) },
            onDelete = { post -> deletePost(post.id) }
        )
        postsRecyclerView.adapter = postsAdapter
    }

    private fun addLike(post: Post) {
        dbHelper.addLike(post.id)
        post.likesCount += 1
        postsAdapter.notifyDataSetChanged()
        Toast.makeText(this, "Liked!", Toast.LENGTH_SHORT).show()
    }

    private fun openComments(postId: Int) {
        val intent = Intent(this, CommentsActivity::class.java)
        intent.putExtra("POST_ID", postId)
        startActivity(intent)
    }

    private fun editPost(postId: Int) {
        val intent = Intent(this, EditPostActivity::class.java)
        intent.putExtra("POST_ID", postId)
        startActivity(intent)
    }

    private fun deletePost(postId: Int) {
        dbHelper.deletePost(postId)
        loadPosts()
        Toast.makeText(this, "Post deleted", Toast.LENGTH_SHORT).show()
    }
}
