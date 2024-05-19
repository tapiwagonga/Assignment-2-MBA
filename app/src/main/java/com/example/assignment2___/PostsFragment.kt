package com.example.assignment2___

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PostsFragment : Fragment() {

    private lateinit var postsRecyclerView: RecyclerView
    private lateinit var postsAdapter: PostsAdapter
    private lateinit var dbHelper: DatabaseHelper
    private var currentUserId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_posts, container, false)

        postsRecyclerView = view.findViewById(R.id.postsRecyclerView)
        postsRecyclerView.layoutManager = LinearLayoutManager(context)
        dbHelper = DatabaseHelper(requireContext())

        // Assume currentUserId is passed via arguments
        currentUserId = arguments?.getInt("USER_ID") ?: -1
        loadPosts()

        return view
    }

    private fun loadPosts() {
        val posts = dbHelper.getAllPosts()
        postsAdapter = PostsAdapter(
            requireContext(),
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
        Toast.makeText(context, "Liked!", Toast.LENGTH_SHORT).show()
    }

    private fun openComments(postId: Int) {
        val intent = Intent(context, CommentsActivity::class.java)
        intent.putExtra("POST_ID", postId)
        startActivity(intent)
    }

    private fun editPost(postId: Int) {
        val intent = Intent(context, EditPostActivity::class.java)
        intent.putExtra("POST_ID", postId)
        startActivity(intent)
    }

    private fun deletePost(postId: Int) {
        dbHelper.deletePost(postId)
        loadPosts()
        Toast.makeText(context, "Post deleted", Toast.LENGTH_SHORT).show()
    }
}
