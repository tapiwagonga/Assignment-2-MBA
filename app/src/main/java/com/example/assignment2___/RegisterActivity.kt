package com.example.assignment2___

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var nameEditText: EditText
    private lateinit var dobEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var backToLoginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dbHelper = DatabaseHelper(this)

        nameEditText = findViewById(R.id.nameEditText)
        dobEditText = findViewById(R.id.dobEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)
        backToLoginBtn = findViewById(R.id.backToLoginBtn)

        dobEditText.setOnClickListener { showDatePickerDialog() }

        registerButton.setOnClickListener { registerUser() }
        backToLoginBtn.setOnClickListener { finish() }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            { _, selectedYear, selectedMonth, selectedDay ->
                dobEditText.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
            }, year, month, day)
        datePickerDialog.show()
    }

    private fun registerUser() {
        val fullName = nameEditText.text.toString().trim()
        val dob = dobEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (fullName.isEmpty() || dob.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val rowId = dbHelper.addUser(fullName, dob, email, password, 0)
        if (rowId != -1L) {
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Registration failed, please try again", Toast.LENGTH_SHORT).show()
        }
    }
}
