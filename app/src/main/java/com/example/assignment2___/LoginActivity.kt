package com.example.assignment2___

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginStudentButton: Button
    private lateinit var loginAdminButton: Button
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginStudentButton = findViewById(R.id.loginStudentButton)
        loginAdminButton = findViewById(R.id.loginAdminButton)
        registerButton = findViewById(R.id.registerButton)

        loginStudentButton.setOnClickListener { loginUser(false) }
        loginAdminButton.setOnClickListener { loginUser(true) }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(isAdmin: Boolean) {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password must not be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val dbHelper = DatabaseHelper(this)
        val user = dbHelper.authenticateUser(email, password, isAdmin)

        if (user != null) {
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("USER_ID", user.id)
            editor.apply()

            if (isAdmin) {
                startActivity(Intent(this, AdminActivity::class.java))
            } else {
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("USER_ID", user.id)  // Pass the user ID to MainActivity
                }
                startActivity(intent)
            }
            finish()
        } else {
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
        }
    }
}
