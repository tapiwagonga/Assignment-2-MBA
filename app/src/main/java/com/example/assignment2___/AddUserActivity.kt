package com.example.assignment2___

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class AddUserActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var dobEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var addButton: Button
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        dbHelper = DatabaseHelper(this)
        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        dobEditText = findViewById(R.id.dobEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        addButton = findViewById(R.id.addButton)
        backButton = findViewById(R.id.backButton)

        dobEditText.setOnClickListener {
            showDatePickerDialog()
        }

        addButton.setOnClickListener {
            val fullName = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val dob = dobEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val success = dbHelper.addUser(fullName, dob, email, password, 0) // isAdmin is 0 for normal users

            if (success != -1L) {
                Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to add user", Toast.LENGTH_SHORT).show()
            }
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
            dobEditText.setText(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }
}
