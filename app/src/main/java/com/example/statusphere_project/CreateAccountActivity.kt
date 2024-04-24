package com.example.statusphere_project
import androidx.appcompat.app.AppCompatActivity
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement
import android.widget.Button
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast


class CreateAccountActivity : AppCompatActivity() {

    private val connectionString = "jdbc:sqlserver://statusphere-server.database.windows.net:1433;databaseName=statusphere-server/Statusphere"
    private val username = "sena"
    private val password = "temp"
    private val passwordTest = "temp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextConfirmPassword = findViewById<EditText>(R.id.editTextConfirmPassword)

        val createAccountButton: Button = findViewById(R.id.buttonCreateAccount)
        createAccountButton.setOnClickListener {
            // Retrieve user input
            val email: String = editTextEmail.text.toString()
            val password: String = editTextPassword.text.toString()
            val passwordTest: String = editTextConfirmPassword.text.toString()

            if (password == passwordTest) {
                createAccount(email, password)
            } else {
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show()
            }


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

        } catch (e: SQLException) {
            e.printStackTrace()
            // Handle database errors (e.g., duplicate email, connection error)
        } finally {
            // Close connection
            connection?.close()
        }
    }
}

