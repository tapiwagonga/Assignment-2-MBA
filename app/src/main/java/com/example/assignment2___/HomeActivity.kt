package com.example.assignment2___

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast

class HomeActivity : AppCompatActivity() {

    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var userAdapter: MyAdapter
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        dbHelper = DatabaseHelper(this)
        usersRecyclerView = findViewById(R.id.usersRecyclerView)
        usersRecyclerView.layoutManager = LinearLayoutManager(this)

        loadUsers()
    }

    private fun loadUsers() {
        val users = dbHelper.getAllUsers()  // This returns List<User>
        val userNames = users.map { it.fullName }  // Extract the full names to pass to MyAdapter
        userAdapter = MyAdapter(userNames,
            onEdit = { userName -> editUser(userName) },
            onDelete = { userName -> deleteUser(userName) }
        )
        usersRecyclerView.adapter = userAdapter
    }

    private fun editUser(userName: String) {
        Toast.makeText(this, "Editing User: $userName", Toast.LENGTH_SHORT).show()
        // Implement editing logic or navigate to an editing screen
    }

    private fun deleteUser(userName: String) {
        Toast.makeText(this, "Deleting User: $userName", Toast.LENGTH_SHORT).show()
        // Implement deletion logic, possibly confirming with the user first
    }
}
