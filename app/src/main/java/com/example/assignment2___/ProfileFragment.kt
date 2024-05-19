package com.example.assignment2___

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProfileFragment : Fragment() {

    private lateinit var fullNameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var dobTextView: TextView
    private lateinit var dateCreatedTextView: TextView
    private lateinit var dateUpdatedTextView: TextView
    private lateinit var postsRecyclerView: RecyclerView
    private lateinit var postsAdapter: PostsAdapter
    private lateinit var dbHelper: DatabaseHelper
    private var currentUserId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        fullNameTextView = view.findViewById(R.id.fullNameTextView)
        emailTextView = view.findViewById(R.id.emailTextView)
        dobTextView = view.findViewById(R.id.dobTextView)
        dateCreatedTextView = view.findViewById(R.id.dateCreatedTextView)
        dateUpdatedTextView = view.findViewById(R.id.dateUpdatedTextView)
        postsRecyclerView = view.findViewById(R.id.postsRecyclerView)
        postsRecyclerView.layoutManager = LinearLayoutManager(context)
        dbHelper = DatabaseHelper(requireContext())

        currentUserId = arguments?.getInt("USER_ID") ?: -1
        loadUserProfile()
        loadUserPosts()

        view.findViewById<Button>(R.id.editProfileButton).setOnClickListener {
            val intent = Intent(context, EditProfileActivity::class.java)
            intent.putExtra("USER_ID", currentUserId)
            startActivity(intent)
        }

        return view
    }

    private fun loadUserProfile() {
        val user = dbHelper.getUserById(currentUserId)
        if (user != null) {
            fullNameTextView.text = user.fullName
            emailTextView.text = user.email
            dobTextView.text = user.dob
            dateCreatedTextView.text = dbHelper.formatDate(user.dateCreated)
            dateUpdatedTextView.text = dbHelper.formatDate(user.dateUpdated)
        } else {
            Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadUserPosts() {
        val posts = dbHelper.getPostsByUserId(currentUserId)
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
        loadUserPosts()
        Toast.makeText(context, "Post deleted", Toast.LENGTH_SHORT).show()
    }
}
