package com.example.assignment2___

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.Toast

class ManageUsersActivity : AppCompatActivity() {

    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_users)

        dbHelper = DatabaseHelper(this)
        usersRecyclerView = findViewById(R.id.usersRecyclerView)
        usersRecyclerView.layoutManager = LinearLayoutManager(this)
        backButton = findViewById(R.id.backButton)

        loadUsers()

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun loadUsers() {
        val users = dbHelper.getAllUsers()
        userAdapter = UserAdapter(users, onEdit = { user ->
            val intent = Intent(this, EditUserActivity::class.java)
            intent.putExtra("USER_ID", user.id)
            startActivity(intent)
        }, onDelete = { user ->
            dbHelper.deleteUser(user.id)
            Toast.makeText(this, "User deleted successfully", Toast.LENGTH_SHORT).show()
            loadUsers() // Reload users
        })
        usersRecyclerView.adapter = userAdapter
    }
}
