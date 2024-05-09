package com.example.assignment2___

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Users table
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS Users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                full_name TEXT NOT NULL,
                dob TEXT NOT NULL,
                secondary_email TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                date_created TEXT NOT NULL,
                date_updated TEXT NOT NULL
            )
        """)

        // Posts table
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS Posts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                author_id INTEGER NOT NULL,
                image_uri TEXT NOT NULL,
                caption TEXT,
                date_created TEXT NOT NULL,
                date_updated TEXT NOT NULL,
                FOREIGN KEY (author_id) REFERENCES Users(id) ON DELETE CASCADE
            )
        """)

        // Comments table
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS Comments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                post_id INTEGER NOT NULL,
                author_id INTEGER NOT NULL,
                comment TEXT NOT NULL,
                date_created TEXT NOT NULL,
                FOREIGN KEY (post_id) REFERENCES Posts(id) ON DELETE CASCADE,
                FOREIGN KEY (author_id) REFERENCES Users(id) ON DELETE CASCADE
            )
        """)

        // Likes table
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS Likes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                post_id INTEGER NOT NULL,
                user_id INTEGER NOT NULL,
                FOREIGN KEY (post_id) REFERENCES Posts(id) ON DELETE CASCADE,
                FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // Apply necessary schema changes here
        }
    }

    fun insertUser(fullName: String, dob: String, email: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("full_name", fullName)
            put("dob", dob)
            put("secondary_email", email)
            put("password", password)
            put("date_created", System.currentTimeMillis().toString())
            put("date_updated", System.currentTimeMillis().toString())
        }
        val result = db.insert("Users", null, values)
        db.close()
        return result != -1L
    }

    fun addComment(postId: Int, userId: Int, comment: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("post_id", postId)
            put("author_id", userId)
            put("comment", comment)
            put("date_created", System.currentTimeMillis().toString())
        }
        db.insert("Comments", null, values)
        db.close()
    }

    fun addLike(postId: Int, userId: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("post_id", postId)
            put("user_id", userId)
        }
        db.insert("Likes", null, values)
        db.close()
    }

    companion object {
        const val DATABASE_NAME = "SchoolApp.db"
        const val DATABASE_VERSION = 1 // Increment this number if you change the database schema.
    }
}
