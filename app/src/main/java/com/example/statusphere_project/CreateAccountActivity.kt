package com.example.statusphere_project
import androidx.appcompat.app.AppCompatActivity
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement
import android.widget.Button
import android.os.Bundle
import android.widget.EditText


class CreateAccountActivity : AppCompatActivity() {

    private val connectionString = "jdbc:sqlserver://statusphere-server.database.windows.net:1433;databaseName=statusphere-server/Statusphere"
    private val username = "sena"
    private val password = "temp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)

        val createAccountButton: Button = findViewById(R.id.buttonCreateAccount)
        createAccountButton.setOnClickListener {
            // Retrieve user input
            val email: String = editTextEmail.text.toString()
            val password: String = editTextPassword.text.toString()

            // Validate user input (e.g., check for empty fields)

            // Create account in database
            createAccount(email, password)
        }
    }

    private fun createAccount(email: String, password: String) {
        var connection: Connection? = null
        try {
            // Establish database connection
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
            connection = DriverManager.getConnection(connectionString, username, password)

            // Execute SQL query to insert new account
            val statement: Statement = connection.createStatement()
            val query = "INSERT INTO Accounts (Email, Password) VALUES ('$email', '$password')"
            statement.executeUpdate(query)

            // Account created successfully
            // Optionally, you can navigate to another activity or show a success message
        } catch (e: SQLException) {
            e.printStackTrace()
            // Handle database errors (e.g., duplicate email, connection error)
        } finally {
            // Close connection
            connection?.close()
        }
    }
}

