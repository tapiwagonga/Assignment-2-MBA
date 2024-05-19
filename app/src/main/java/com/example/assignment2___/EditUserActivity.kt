package com.example.assignment2___

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditUserActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var dobEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var saveButton: Button
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

        userId = intent.getIntExtra("USER_ID", 0)
        dbHelper = DatabaseHelper(this)

        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        dobEditText = findViewById(R.id.dobEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        saveButton = findViewById(R.id.saveButton)

        loadUserDetails()

        saveButton.setOnClickListener {
            saveUserDetails()
        }
    }

    private fun loadUserDetails() {
        val user = dbHelper.getUserById(userId)
        user?.let { currentUser ->
            nameEditText.setText(currentUser.fullName)
            emailEditText.setText(currentUser.email)
            dobEditText.setText(currentUser.dob)
            passwordEditText.setText(currentUser.password)
        }
    }

    private fun saveUserDetails() {
        val fullName = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val dob = dobEditText.text.toString()
        val password = passwordEditText.text.toString()

        val success = dbHelper.updateUser(userId, fullName, dob, email, password)
        if (success > 0) {
            Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Failed to update user", Toast.LENGTH_SHORT).show()
        }
    }
}
