package com.example.statusphere_project
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement
import android.widget.TextView
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.ViewModel


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
        val textViewPasswordStrength = findViewById<TextView>(R.id.textViewPasswordStrength)


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

class CreateAccountViewModel : ViewModel() {
    private lateinit var textViewPasswordStrength: TextView


    fun onPasswordChanged(password: Editable) {
        val strength = calculatePasswordStrength(password.toString())

        when (strength) {
            0 -> {
                // Weak
                textViewPasswordStrength.text = "Weak"
                textViewPasswordStrength.setTextColor(Color.RED)
            }
            1 -> {
                // Medium
                textViewPasswordStrength.text = "Medium"
                textViewPasswordStrength.setTextColor(Color.YELLOW)
            }
            2 -> {
                // Strong
                textViewPasswordStrength.text = "Strong"
                textViewPasswordStrength.setTextColor(Color.GREEN)
            }
        }
    }

    private fun calculatePasswordStrength(password: String): Int {
        // Check password length
        val length = password.length
        if (length < 6) {
            // Password is too short, considered weak
            return 0 // Weak
        }

        // Check for the presence of uppercase and lowercase letters
        val hasUppercase = password != password.lowercase()
        val hasLowercase = password != password.uppercase()

        // Check for the presence of numbers
        var hasDigit = false
        for (c in password) {
            if (c.isDigit()) {
                hasDigit = true
                break
            }
        }

        // Check for the presence of special characters
        val hasSpecialChar = !password.matches("[A-Za-z0-9 ]*".toRegex())

        // Combine the criteria to determine password strength
        val strength: Int
        strength = when {
            length >= 10 -> 2 // Strong password (length >= 10)
            length >= 8 && (hasUppercase || hasLowercase) && hasDigit && hasSpecialChar -> 1 // Medium password (length >= 8, contains uppercase, lowercase, digit, and special character)
            else -> 0 // Weak password
        }
        return strength
    }
}



