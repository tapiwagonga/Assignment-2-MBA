package com.example.assignment2___

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NewPostActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var captionEditText: EditText
    private lateinit var uploadButton: Button
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        imageView = findViewById(R.id.imageView)
        progressBar = findViewById(R.id.progressBar)
        captionEditText = findViewById(R.id.captionEditText)
        uploadButton = findViewById(R.id.uploadButton)
        submitButton = findViewById(R.id.submitButton)

        uploadButton.setOnClickListener {
            val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhotoIntent, PICK_IMAGE_REQUEST)
        }

        submitButton.setOnClickListener {
            savePost()
        }
    }

    private fun savePost() {
        val caption = captionEditText.text.toString()
        if (caption.isEmpty()) {
            Toast.makeText(this, "Caption cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE
        // Simulate a network operation or database insertion
        Handler().postDelayed({
            progressBar.visibility = View.GONE
            Toast.makeText(this, "Post added successfully", Toast.LENGTH_SHORT).show()
            finish()  // Close activity and return to the previous one
        }, 2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage = data.data
            imageView.setImageURI(selectedImage)
            submitButton.isEnabled = true
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}
