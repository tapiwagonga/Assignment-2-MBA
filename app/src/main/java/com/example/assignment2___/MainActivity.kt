package com.example.assignment2___

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity(), PostsAdapter.PostInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragment_container, PostsFragment())
            }
        }
    }

    override fun onLikeClicked(postId: Int) {
        // Implement like functionality here
    }

    override fun onCommentClicked(postId: Int) {
        // Implement comment functionality here
    }
}
