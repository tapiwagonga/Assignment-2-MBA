package com.example.assignment2___

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var saveProfileButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        saveProfileButton = findViewById(R.id.saveProfileButton)

        saveProfileButton.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        // Implement your save logic here
        // This might involve saving to SharedPreferences or a database
    }
}
