package com.example.statusphere_project
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.sql.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import android.app.ActivityOptions



class LoginActivity : AppCompatActivity() {

    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    var userId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val createAccountButton: Button = findViewById(R.id.buttonCreateAccount)
        // Set click listener for the button
        createAccountButton.setOnClickListener {
            // Navigate to the Create Account activity
            val intent = Intent(this, CreateAccountActivity::class.java)
            val options = ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            startActivity(intent, options.toBundle())
        }


            editTextUsername = findViewById(R.id.editTextUsername)
            editTextPassword = findViewById(R.id.editTextPassword)
            buttonLogin = findViewById(R.id.buttonLogin)

            buttonLogin.setOnClickListener {
                val username = editTextUsername.text.toString()
                val password = editTextPassword.text.toString()


                // Check if username and password are correct (for demo purpose, we'll just check if they're not empty)
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    // Navigate to the main activity


                    val intent = Intent(this, HomeActivity::class.java)
                    val options = ActivityOptions.makeCustomAnimation(
                        this,
                        R.anim.slide_in_right,
                        R.anim.slide_out_left
                    )

                    intent.putExtra("userId", userId)

                    startActivity(intent, options.toBundle())


                } else {
                    // Show an error message if username or password is empty
                    Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        
    }

    private fun saveUserId(userId: Int) {
        // Save the userId to SharedPreferences or any other storage mechanism

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("userId", userId)
        editor.apply()
    }

    class DatabaseHelper {

        private val connectionString =
            "jdbc:sqlserver://statusphere-server.database.windows.net:1433;databaseName=statusphere-server"
        private val username = "sena"
        private val password = "temp"

        fun searchFriendByEmail(email: String): Boolean {
            var friendFound = false
            var connection: Connection? = null
            var preparedStatement: PreparedStatement? = null
            var resultSet: ResultSet? = null

            try {
                // Establish connection
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
                connection = DriverManager.getConnection(connectionString, username, password)

                // Prepare SQL statement
                val query = "SELECT COUNT(*) FROM Users WHERE Email = ?"
                preparedStatement = connection.prepareStatement(query)
                preparedStatement.setString(1, email)

                // Execute query
                resultSet = preparedStatement.executeQuery()

                // Check if friend is found
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    friendFound = true
                }
            } catch (e: SQLException) {
                e.printStackTrace()
                // Handle database errors
            } finally {
                // Close resources
                resultSet?.close()
                preparedStatement?.close()
                connection?.close()
            }

            return friendFound
        }

        fun authenticateUser(username: String, password: String): Boolean {
            var isAuthenticated = false
            var connection: Connection? = null

            try {
                // Establish connection
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
                connection = DriverManager.getConnection(connectionString, this.username, this.password)

                // Execute authentication query
                val query =
                    "SELECT COUNT(*) FROM Accounts.UserAccounts WHERE Username = ? AND Password = ?"
                val preparedStatement = connection.prepareStatement(query)
                preparedStatement.setString(1, username)
                preparedStatement.setString(2, password)
                val resultSet = preparedStatement.executeQuery()

                // Check authentication result
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    isAuthenticated = true
                }
            } catch (e: SQLException) {
                e.printStackTrace()
                // Handle database errors
            } finally {
                connection?.let { conn ->
                    try {
                        // Close connection
                        conn.close()
                    } catch (ex: SQLException) {
                        ex.printStackTrace()
                        // Handle closing connection error
                    }
                }
            }

            return isAuthenticated
        }
    }

}



/*class DatabaseHelper {

    fun connectToDatabase(): Connection? {
        val connectionUrl = "jdbc:sqlserver://statusphere-server.database.windows.net;databaseName=statusphere-server/Statusphere;user=sena;password=your_password;"
        var connection: Connection? = null

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
            connection = DriverManager.getConnection(connectionUrl)
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return connection
    }

    fun authenticateUser(username: String, password: String): Boolean {
        var isAuthenticated = false
        val connection = connectToDatabase()

        if (connection != null) {
            try {
                val statement = connection.prepareStatement("SELECT * FROM Users WHERE username = ? AND password = ?")
                statement.setString(1, username)
                statement.setString(2, password)
                val resultSet = statement.executeQuery()
                if (resultSet.next()) {
                    // User authenticated successfully
                    isAuthenticated = true
                }
                statement.close()
                connection.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }

        return isAuthenticated
    }
}

 */
