package com.example.assignment2___

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val btnAddUser = findViewById<Button>(R.id.btnAddUser)
        val btnManageUsers = findViewById<Button>(R.id.btnManageUsers)
        val btnBack = findViewById<Button>(R.id.btnBack)

        btnAddUser.setOnClickListener {
            // Navigate to AddUserActivity
            val intent = Intent(this, AddUserActivity::class.java)
            startActivity(intent)
        }

        btnManageUsers.setOnClickListener {
            // Navigate to ManageUsersActivity
            val intent = Intent(this, ManageUsersActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            // Go back to the previous activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
