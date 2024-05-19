package com.example.assignment2___

data class User(
    val id: Int,
    val fullName: String,
    val dob: String,
    val email: String,
    val password: String,
    val isAdmin: Boolean,
    val dateCreated: Long,
    val dateUpdated: Long
)
