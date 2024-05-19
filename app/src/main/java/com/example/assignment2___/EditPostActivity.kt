package com.example.assignment2___

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditPostActivity : AppCompatActivity() {
    private lateinit var captionEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var dbHelper: DatabaseHelper
    private var postId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_post)

        captionEditText = findViewById(R.id.captionEditText)
        saveButton = findViewById(R.id.saveButton)
        dbHelper = DatabaseHelper(this)

        // Get the post ID from the intent
        postId = intent.getIntExtra("POST_ID", 0)
        if (postId == 0) {
            Toast.makeText(this, "Invalid post ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Load the existing post details
        val post = dbHelper.getPostById(postId)
        post?.let {
            captionEditText.setText(it.content)
        }

        saveButton.setOnClickListener {
            val newCaption = captionEditText.text.toString()
            if (newCaption.isNotBlank()) {
                dbHelper.updatePostCaption(postId, newCaption)
                Toast.makeText(this, "Post updated", Toast.LENGTH_SHORT).show()
                finish() // Close the activity after saving
            } else {
                Toast.makeText(this, "Caption cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
