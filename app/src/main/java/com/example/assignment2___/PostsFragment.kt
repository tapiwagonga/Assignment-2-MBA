package com.example.assignment2___

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PostsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostsAdapter
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_posts, container, false)
        dbHelper = DatabaseHelper(requireContext())
        recyclerView = view.findViewById(R.id.postsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        loadPosts()

        return view
    }

    private fun loadPosts() {
        val posts = dbHelper.getAllPosts()
        adapter = PostsAdapter(
            posts,
            onLike = { postId ->
                dbHelper.addLike(postId)
                val post = posts.find { it.id == postId }
                post?.likesCount = post?.likesCount?.plus(1) ?: 0
                adapter.notifyItemChanged(posts.indexOf(post))
            },
            onComment = { postId ->
                val intent = Intent(context, CommentsActivity::class.java).apply {
                    putExtra("POST_ID", postId)
                }
                startActivity(intent)
            },
            onEdit = { post ->
                val intent = Intent(context, EditPostActivity::class.java).apply {
                    putExtra("POST_ID", post.id)
                }
                startActivity(intent)
            },
            onDelete = { postId ->
                dbHelper.deletePost(postId)
                loadPosts() // Reload posts to reflect deletion
            }
        )
        recyclerView.adapter = adapter
    }
}
