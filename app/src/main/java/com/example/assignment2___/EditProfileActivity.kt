package com.example.assignment2___

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var userId: Int = 0
    private lateinit var fullNameEditText: EditText
    private lateinit var dobEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        dbHelper = DatabaseHelper(this)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        userId = sharedPreferences.getInt("USER_ID", 0)

        fullNameEditText = findViewById(R.id.fullNameEditText)
        dobEditText = findViewById(R.id.dobEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        saveButton = findViewById(R.id.saveButton)

        loadUserProfile()

        dobEditText.setOnClickListener {
            showDatePickerDialog()
        }

        saveButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString().trim()
            val dob = dobEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (fullName.isEmpty() || dob.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val result = dbHelper.updateUser(userId, fullName, dob, email, password)
            if (result > 0) {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Profile update failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadUserProfile() {
        val user = dbHelper.getUserById(userId)
        if (user != null) {
            fullNameEditText.setText(user.fullName)
            dobEditText.setText(user.dob)
            emailEditText.setText(user.email)
            passwordEditText.setText(user.password)
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                dobEditText.setText(sdf.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}
