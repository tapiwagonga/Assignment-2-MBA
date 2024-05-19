package com.example.assignment2___

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewPostActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var captionEditText: EditText
    private lateinit var uploadButton: Button
    private lateinit var submitButton: Button
    private lateinit var backButton: Button
    private lateinit var bottomNavigationView: BottomNavigationView
    private var imageUri: Uri? = null
    private var userId: Int = 1 // Replace this with the actual user ID logic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        // Inflate the new post content into the content frame
        val inflater = LayoutInflater.from(this)
        val newPostContent = inflater.inflate(R.layout.new_post_content, null)
        findViewById<FrameLayout>(R.id.content_frame).addView(newPostContent)

        imageView = findViewById(R.id.imageView)
        progressBar = findViewById(R.id.progressBar)
        captionEditText = findViewById(R.id.captionEditText)
        uploadButton = findViewById(R.id.uploadButton)
        submitButton = findViewById(R.id.submitButton)
        backButton = findViewById(R.id.backButton)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        uploadButton.setOnClickListener {
            val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhotoIntent, PICK_IMAGE_REQUEST)
        }

        submitButton.setOnClickListener {
            savePost()
        }

        backButton.setOnClickListener {
            finish() // Go back to the previous activity
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_posts -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.navigation_create_post -> {
                    // Current activity, do nothing
                    true
                }
                else -> false
            }
        }

        // Set the current selected item
        bottomNavigationView.selectedItemId = R.id.navigation_create_post
    }

    private fun savePost() {
        val caption = captionEditText.text.toString().trim()
        if (caption.isEmpty()) {
            Toast.makeText(this, "Caption cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (imageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE
        Handler().postDelayed({
            val dbHelper = DatabaseHelper(this)
            dbHelper.addPost(userId, caption, imageUri.toString()) // Pass the imageUri here
            progressBar.visibility = View.GONE
            Toast.makeText(this, "Post added successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            imageView.setImageURI(imageUri)
            submitButton.isEnabled = true
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}
