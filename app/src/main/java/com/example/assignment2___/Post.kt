package com.example.assignment2___

data class Post(
    val id: Int,
    val authorId: Int,
    var content: String,
    var imageUri: String, // Add this line
    var likesCount: Int = 0,
    var commentsCount: Int = 0,
    var dateCreated: Long = 0,
    var dateUpdated: Long = 0,
    var isEdited: Boolean = false
)
