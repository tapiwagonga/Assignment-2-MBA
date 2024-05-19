package com.example.assignment2___

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProfileFragment : Fragment() {
    private lateinit var dbHelper: DatabaseHelper
    private var userId: Int = 0
    private lateinit var fullNameTextView: TextView
    private lateinit var dobTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var dateCreatedTextView: TextView
    private lateinit var dateUpdatedTextView: TextView
    private lateinit var editProfileButton: Button
    private lateinit var postsRecyclerView: RecyclerView
    private lateinit var postsAdapter: PostsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        fullNameTextView = view.findViewById(R.id.fullNameTextView)
        dobTextView = view.findViewById(R.id.dobTextView)
        emailTextView = view.findViewById(R.id.emailTextView)
        dateCreatedTextView = view.findViewById(R.id.dateCreatedTextView)
        dateUpdatedTextView = view.findViewById(R.id.dateUpdatedTextView)
        editProfileButton = view.findViewById(R.id.editProfileButton)
        postsRecyclerView = view.findViewById(R.id.postsRecyclerView)

        dbHelper = DatabaseHelper(requireContext())

        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        userId = sharedPreferences.getInt("USER_ID", 0)

        loadUserProfile()
        setupPostsRecyclerView()

        editProfileButton.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun loadUserProfile() {
        val user = dbHelper.getUserById(userId)
        if (user != null) {
            fullNameTextView.text = user.fullName
            dobTextView.text = user.dob
            emailTextView.text = user.email
            dateCreatedTextView.text = "Date Created: ${dbHelper.formatDate(user.dateCreated)}"
            dateUpdatedTextView.text = "Date Updated: ${dbHelper.formatDate(user.dateUpdated)}"
        }
    }

    private fun setupPostsRecyclerView() {
        val posts = dbHelper.getPostsByUserId(userId)
        postsAdapter = PostsAdapter(posts,
            onLike = { postId ->
                dbHelper.addLike(postId)
                refreshPosts()
            },
            onComment = { postId ->
                // Handle view comments
                val intent = Intent(requireContext(), CommentsActivity::class.java)
                intent.putExtra("POST_ID", postId)
                startActivity(intent)
            },
            onEdit = { post ->
                dbHelper.updatePostCaption(post.id, post.content)
                refreshPosts()
            },
            onDelete = { postId ->
                dbHelper.deletePost(postId)
                refreshPosts()
            }
        )
        postsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        postsRecyclerView.adapter = postsAdapter
    }

    private fun refreshPosts() {
        val posts = dbHelper.getPostsByUserId(userId)
        postsAdapter.updatePosts(posts)
    }
}
