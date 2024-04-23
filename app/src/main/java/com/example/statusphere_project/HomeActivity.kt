package com.example.statusphere_project

import android.os.Bundle
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.statusphere_project.LoginActivity.DatabaseHelper
import android.widget.Button
import java.sql.SQLException
import android.widget.Toast
import android.widget.EditText
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener


class HomeActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var addButton: Button
    private lateinit var editTextEmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        MobileAds.initialize(this, OnInitializationCompleteListener {

        })
        val dao = DatabaseDAO()
        addButton = findViewById(R.id.btnAddFriend)

        val userId = intent.getIntExtra("userId", -1) // -1 is a default value if userId is not found
        databaseHelper = DatabaseHelper()
        addButton.setOnClickListener {
            val email = editTextEmail.text.toString().trim()

            // Perform a search in the database for the provided email address
            val isFriendFound = databaseHelper.searchFriendByEmail(email)

            if (isFriendFound) {
                // Friend found in the database, perform appropriate action (e.g., add friend to list)
                Toast.makeText(this, "Friend found and added!", Toast.LENGTH_SHORT).show()
            } else {
                // Friend not found in the database
                Toast.makeText(this, "Friend not found!", Toast.LENGTH_SHORT).show()
            }
        if (userId != -1) {
            // Use userId as needed in your HomeActivity

        } else {
            // Handle the case where userId is not found
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        val friends = dao.getFriends(userId)
        val groups = dao.getGroups(userId)



        // Add code to initialize and display user's friends, groups, and sphere visualization
    }
}

class DatabaseDAO {

    // JDBC connection parameters
    private val url = "jdbc:mysql://statusphere-server.database.windows.net/statusphere-server/Statusphere"
    private val username = "sena"
    private val password = "temp"

    // Method to fetch user's friends
    fun getFriends(userId: Int): List<String> {
        val friends = mutableListOf<String>()

        // Establish database connection
        DriverManager.getConnection(url, username, password).use { connection ->
            // Prepare SQL query
            val sql = "SELECT name FROM friends WHERE user_id = ?"
            val statement = connection.prepareStatement(sql)

            // Set parameters
            statement.setInt(1, userId)

            // Execute query
            val resultSet = statement.executeQuery()

            // Process result set
            while (resultSet.next()) {
                val friendName = resultSet.getString("name")
                friends.add(friendName)
            }
        }

        return friends
    }

    // Method to fetch user's groups
    fun getGroups(userId: Int): List<String> {
        val groups = mutableListOf<String>()

        // Establish database connection
        DriverManager.getConnection(url, username, password).use { connection ->
            // Prepare SQL query
            val sql = "SELECT name FROM groups WHERE user_id = ?"
            val statement = connection.prepareStatement(sql)

            // Set parameters
            statement.setInt(1, userId)

            // Execute query
            val resultSet = statement.executeQuery()

            // Process result set
            while (resultSet.next()) {
                val groupName = resultSet.getString("name")
                groups.add(groupName)
            }
        }

        return groups
    }
}

class SphereView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE // Set the sphere color
        style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw the sphere
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), 100f, paint)
    }
    }
}