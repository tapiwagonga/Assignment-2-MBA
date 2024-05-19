package com.example.assignment2___

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private var currentUserId: Int = -1  // Variable to store the current user's ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentUserId = intent.getIntExtra("USER_ID", -1)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_posts -> {
                    replaceFragment(PostsFragment())
                    true
                }
                R.id.navigation_profile -> {
                    val fragment = ProfileFragment().apply {
                        arguments = Bundle().apply {
                            putInt("USER_ID", currentUserId)  // Pass the user ID to ProfileFragment
                        }
                    }
                    replaceFragment(fragment)
                    true
                }
                R.id.navigation_create_post -> {
                    val intent = Intent(this, NewPostActivity::class.java)
                    intent.putExtra("USER_ID", currentUserId) // Pass the user ID to NewPostActivity
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        // Set default selection
        bottomNavigationView.selectedItemId = R.id.navigation_posts
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
