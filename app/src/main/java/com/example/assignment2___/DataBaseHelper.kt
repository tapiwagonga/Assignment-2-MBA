package com.example.assignment2___

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "schoolApp.db"
        const val DATABASE_VERSION = 3
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
        CREATE TABLE Users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            full_name TEXT NOT NULL,
            dob TEXT NOT NULL,
            email TEXT NOT NULL UNIQUE,
            password TEXT NOT NULL,
            is_admin INTEGER DEFAULT 0,
            date_created INTEGER DEFAULT 0,
            date_updated INTEGER DEFAULT 0
        )
        """)

        db.execSQL("""
        CREATE TABLE Posts (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            author_id INTEGER NOT NULL,
            content TEXT NOT NULL,
            image_uri TEXT,
            likes_count INTEGER DEFAULT 0,
            comments_count INTEGER DEFAULT 0,
            date_created INTEGER DEFAULT 0,
            date_updated INTEGER DEFAULT 0,
            is_edited INTEGER DEFAULT 0,
            FOREIGN KEY (author_id) REFERENCES Users(id)
        )
        """)

        db.execSQL("""
        CREATE TABLE Comments (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            post_id INTEGER NOT NULL,
            author_id INTEGER NOT NULL,
            content TEXT NOT NULL,
            date_created INTEGER DEFAULT 0,
            likes_count INTEGER DEFAULT 0,
            dislikes_count INTEGER DEFAULT 0,
            FOREIGN KEY (post_id) REFERENCES Posts(id),
            FOREIGN KEY (author_id) REFERENCES Users(id)
        )
        """)

        db.execSQL("""
        CREATE TABLE ChatMessages (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            sender_id INTEGER NOT NULL,
            receiver_id INTEGER NOT NULL,
            message TEXT NOT NULL,
            timestamp LONG NOT NULL,
            FOREIGN KEY (sender_id) REFERENCES Users(id),
            FOREIGN KEY (receiver_id) REFERENCES Users(id)
        )
        """)

        insertAdmin(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE Posts ADD COLUMN likes_count INTEGER DEFAULT 0")
            db.execSQL("ALTER TABLE Posts ADD COLUMN comments_count INTEGER DEFAULT 0")
        }
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE Posts ADD COLUMN is_edited INTEGER DEFAULT 0")
        }
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE Posts ADD COLUMN image_uri TEXT NOT NULL DEFAULT ''")
        }
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS Users")
            db.execSQL("DROP TABLE IF EXISTS Posts")
            db.execSQL("DROP TABLE IF EXISTS Comments")
            onCreate(db)
        }
    }

    private fun insertAdmin(db: SQLiteDatabase) {
        val values = ContentValues().apply {
            put("full_name", "Admin")
            put("dob", "1900-01-01")
            put("email", "admin@admin.com")
            put("password", "admin123")
            put("is_admin", 1)
            put("date_created", System.currentTimeMillis())
            put("date_updated", System.currentTimeMillis())
        }
        db.insert("Users", null, values)
    }

    fun getUserNameById(userId: Int): String? {
        val db = this.readableDatabase
        val cursor = db.query(
            "Users",
            arrayOf("full_name"),
            "id = ?",
            arrayOf(userId.toString()),
            null, null, null
        )
        var userName: String? = null
        if (cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndexOrThrow("full_name"))
        }
        cursor.close()
        return userName
    }



    fun addComment(context: Context, postId: Int, authorId: Int, content: String): Comment? {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("post_id", postId)
            put("author_id", authorId)
            put("content", content)
            put("date_created", System.currentTimeMillis())
        }
        val commentId = db.insert("Comments", null, values)

        // Send notification
        NotificationUtils.sendNotification(context, "New Comment", "Someone commented on your post.")

        if (commentId == -1L) {
            return null
        }

        return Comment(
            id = commentId.toInt(),
            postId = postId,
            authorId = authorId,
            authorName = getUserNameById(authorId) ?: "Unknown",
            content = content,
            dateCreated = System.currentTimeMillis(),
            likesCount = 0,
            dislikesCount = 0
        )
    }





    fun addLikeToComment(commentId: Int) {
        val db = writableDatabase
        db.execSQL("UPDATE Comments SET likes_count = likes_count + 1 WHERE id = ?", arrayOf(commentId))
    }

    fun addDislikeToComment(commentId: Int) {
        val db = writableDatabase
        db.execSQL("UPDATE Comments SET dislikes_count = dislikes_count + 1 WHERE id = ?", arrayOf(commentId))
    }


    fun authenticateUser(email: String, password: String, isAdmin: Boolean): User? {
        val db = this.readableDatabase
        val cursor = db.query(
            "Users",
            arrayOf("id", "full_name", "dob", "email", "password", "is_admin", "date_created", "date_updated"),
            "email = ? AND password = ? AND is_admin = ?",
            arrayOf(email, password, if (isAdmin) "1" else "0"),
            null, null, null
        )

        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                fullName = cursor.getString(cursor.getColumnIndexOrThrow("full_name")),
                dob = cursor.getString(cursor.getColumnIndexOrThrow("dob")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                password = cursor.getString(cursor.getColumnIndexOrThrow("password")),
                isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow("is_admin")) == 1,
                dateCreated = cursor.getLong(cursor.getColumnIndexOrThrow("date_created")),
                dateUpdated = cursor.getLong(cursor.getColumnIndexOrThrow("date_updated"))
            )
        }
        cursor.close()
        db.close()
        return user
    }

    fun getCommentById(commentId: Int): Comment? {
        val db = readableDatabase
        val cursor = db.query(
            "Comments",
            arrayOf("id", "post_id", "author_id", "author_name", "content", "likes_count", "dislikes_count", "date_created"),
            "id = ?",
            arrayOf(commentId.toString()),
            null, null, null
        )

        var comment: Comment? = null
        if (cursor.moveToFirst()) {
            comment = Comment(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                postId = cursor.getInt(cursor.getColumnIndexOrThrow("post_id")),
                authorId = cursor.getInt(cursor.getColumnIndexOrThrow("author_id")),
                authorName = cursor.getString(cursor.getColumnIndexOrThrow("author_name")),
                content = cursor.getString(cursor.getColumnIndexOrThrow("content")),
                likesCount = cursor.getInt(cursor.getColumnIndexOrThrow("likes_count")),
                dislikesCount = cursor.getInt(cursor.getColumnIndexOrThrow("dislikes_count")),
                dateCreated = cursor.getLong(cursor.getColumnIndexOrThrow("date_created"))
            )
        }
        cursor.close()
        return comment
    }


    fun getAllUsers(): List<User> {
        val users = mutableListOf<User>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Users", null)
        if (cursor.moveToFirst()) {
            do {
                users.add(User(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    fullName = cursor.getString(cursor.getColumnIndexOrThrow("full_name")),
                    dob = cursor.getString(cursor.getColumnIndexOrThrow("dob")),
                    email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    password = cursor.getString(cursor.getColumnIndexOrThrow("password")),
                    isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow("is_admin")) == 1,
                    dateCreated = cursor.getLong(cursor.getColumnIndexOrThrow("date_created")),
                    dateUpdated = cursor.getLong(cursor.getColumnIndexOrThrow("date_updated"))
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return users
    }


    fun getUserById(userId: Int): User? {
        val db = this.readableDatabase
        val cursor = db.query(
            "Users",
            arrayOf("id", "full_name", "dob", "email", "password", "is_admin", "date_created", "date_updated"),
            "id = ?",
            arrayOf(userId.toString()),
            null, null, null
        )
        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                fullName = cursor.getString(cursor.getColumnIndexOrThrow("full_name")),
                dob = cursor.getString(cursor.getColumnIndexOrThrow("dob")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                password = cursor.getString(cursor.getColumnIndexOrThrow("password")),
                isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow("is_admin")) == 1,
                dateCreated = cursor.getLong(cursor.getColumnIndexOrThrow("date_created")),
                dateUpdated = cursor.getLong(cursor.getColumnIndexOrThrow("date_updated"))
            )
        }
        cursor.close()
        db.close()
        return user
    }

    fun updatePost(id: Int, content: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("content", content)
            put("date_updated", System.currentTimeMillis())
            put("is_edited", 1)
        }
        return db.update("Posts", values, "id = ?", arrayOf(id.toString()))
    }

    fun addUser(fullName: String, dob: String, email: String, password: String, isAdmin: Int): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("full_name", fullName)
            put("dob", dob)
            put("email", email)
            put("password", password)
            put("is_admin", isAdmin)
            put("date_created", System.currentTimeMillis())
            put("date_updated", System.currentTimeMillis())
        }
        return db.insert("Users", null, values)
    }

    fun updateUser(id: Int, fullName: String, dob: String, email: String, password: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("full_name", fullName)
            put("dob", dob)
            put("email", email)
            put("password", password)
            put("date_updated", System.currentTimeMillis())
        }
        return db.update("Users", values, "id = ?", arrayOf(id.toString()))
    }

    fun getPostById(postId: Int): Post? {
        val db = this.readableDatabase
        val cursor = db.query(
            "Posts",
            arrayOf("id", "author_id", "content", "image_uri", "likes_count", "comments_count", "date_created", "date_updated", "is_edited"),
            "id = ?",
            arrayOf(postId.toString()),
            null, null, null
        )

        var post: Post? = null
        if (cursor.moveToFirst()) {
            post = Post(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                authorId = cursor.getInt(cursor.getColumnIndexOrThrow("author_id")),
                content = cursor.getString(cursor.getColumnIndexOrThrow("content")),
                imageUri = cursor.getString(cursor.getColumnIndexOrThrow("image_uri")), // Retrieve the image URI
                likesCount = cursor.getInt(cursor.getColumnIndexOrThrow("likes_count")),
                commentsCount = cursor.getInt(cursor.getColumnIndexOrThrow("comments_count")),
                dateCreated = cursor.getLong(cursor.getColumnIndexOrThrow("date_created")),
                dateUpdated = cursor.getLong(cursor.getColumnIndexOrThrow("date_updated")),
                isEdited = cursor.getInt(cursor.getColumnIndexOrThrow("is_edited")) == 1
            )
        }
        cursor.close()
        return post
    }


    fun updatePostCaption(postId: Int, newCaption: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("content", newCaption)
            put("date_updated", System.currentTimeMillis())
        }

        val result = db.update("Posts", contentValues, "id = ?", arrayOf(postId.toString()))
        return result > 0
    }

    fun getPostsByUserId(userId: Int): List<Post> {
        val posts = mutableListOf<Post>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Posts WHERE author_id = ?", arrayOf(userId.toString()))
        if (cursor.moveToFirst()) {
            do {
                posts.add(Post(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    authorId = cursor.getInt(cursor.getColumnIndexOrThrow("author_id")),
                    content = cursor.getString(cursor.getColumnIndexOrThrow("content")),
                    imageUri = cursor.getString(cursor.getColumnIndexOrThrow("image_uri")), // Retrieve the image URI
                    likesCount = cursor.getInt(cursor.getColumnIndexOrThrow("likes_count")),
                    commentsCount = cursor.getInt(cursor.getColumnIndexOrThrow("comments_count")),
                    dateCreated = cursor.getLong(cursor.getColumnIndexOrThrow("date_created")),
                    dateUpdated = cursor.getLong(cursor.getColumnIndexOrThrow("date_updated")),
                    isEdited = cursor.getInt(cursor.getColumnIndexOrThrow("is_edited")) == 1
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return posts
    }

    fun deletePost(id: Int): Int {
        val db = this.writableDatabase
        return db.delete("Posts", "id = ?", arrayOf(id.toString()))
    }

    fun getAllPosts(): List<Post> {
        val posts = mutableListOf<Post>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Posts", null)
        if (cursor.moveToFirst()) {
            do {
                posts.add(
                    Post(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        authorId = cursor.getInt(cursor.getColumnIndexOrThrow("author_id")),
                        content = cursor.getString(cursor.getColumnIndexOrThrow("content")),
                        imageUri = cursor.getString(cursor.getColumnIndexOrThrow("image_uri")), // Add this line
                        likesCount = cursor.getInt(cursor.getColumnIndexOrThrow("likes_count")),
                        commentsCount = cursor.getInt(cursor.getColumnIndexOrThrow("comments_count")),
                        dateCreated = cursor.getLong(cursor.getColumnIndexOrThrow("date_created")),
                        dateUpdated = cursor.getLong(cursor.getColumnIndexOrThrow("date_updated")),
                        isEdited = cursor.getInt(cursor.getColumnIndexOrThrow("is_edited")) == 1
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return posts
    }





    private fun getCurrentDateTime(): Long {
        return System.currentTimeMillis()
    }

    fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val date = Date(timestamp)
        return sdf.format(date)
    }



    fun addLike(postId: Int) {
        val db = writableDatabase
        db.execSQL("UPDATE Posts SET likes_count = likes_count + 1 WHERE id = ?", arrayOf(postId))
    }

    fun getCommentsByPostId(postId: Int): List<Comment> {
        val comments = mutableListOf<Comment>()
        val db = this.readableDatabase
        val query = """
        SELECT Comments.id, Comments.post_id, Comments.author_id, Comments.content, Comments.date_created, 
               Comments.likes_count, Comments.dislikes_count, Users.full_name AS author_name
        FROM Comments
        JOIN Users ON Comments.author_id = Users.id
        WHERE Comments.post_id = ?
    """
        val cursor = db.rawQuery(query, arrayOf(postId.toString()))
        if (cursor.moveToFirst()) {
            do {
                comments.add(Comment(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    postId = cursor.getInt(cursor.getColumnIndexOrThrow("post_id")),
                    authorId = cursor.getInt(cursor.getColumnIndexOrThrow("author_id")),
                    authorName = cursor.getString(cursor.getColumnIndexOrThrow("author_name")),  // Add this line
                    content = cursor.getString(cursor.getColumnIndexOrThrow("content")),
                    dateCreated = cursor.getLong(cursor.getColumnIndexOrThrow("date_created")),
                    likesCount = cursor.getInt(cursor.getColumnIndexOrThrow("likes_count")),
                    dislikesCount = cursor.getInt(cursor.getColumnIndexOrThrow("dislikes_count"))
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return comments
    }


    fun deleteUser(id: Int): Int {
        val db = this.writableDatabase
        return db.delete("Users", "id = ?", arrayOf(id.toString()))
    }

    fun sendNotification(context: Context, title: String, message: String) {
        val builder = NotificationCompat.Builder(context, NotificationUtils.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }


    fun addPost(authorId: Int, content: String, imageUri: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("author_id", authorId)
            put("content", content)
            put("image_uri", imageUri)
            put("likes_count", 0)
            put("comments_count", 0)
            put("date_created", System.currentTimeMillis())
            put("date_updated", System.currentTimeMillis())
            put("is_edited", 0)
        }
        return db.insert("Posts", null, values)
    }
}








