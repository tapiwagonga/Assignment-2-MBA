package com.example.assignment2___

data class Comment(
    val id: Int,
    val postId: Int,
    val authorId: Int,
    val authorName: String,  // Add this line
    val content: String,
    val dateCreated: Long,
    var likesCount: Int = 0,
    var dislikesCount: Int = 0
)
