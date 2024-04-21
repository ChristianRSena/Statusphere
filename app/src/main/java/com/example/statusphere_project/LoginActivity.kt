import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.statusphere_project.R
import java.sql.*

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)

        buttonLogin.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()

            // Check if username and password are correct (for demo purpose, we'll just check if they're not empty)
            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Navigate to the main activity or perform any other action
                // For now, let's just display a toast message
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                // You can replace the Toast with an Intent to navigate to another activity
                // val intent = Intent(this, MainActivity::class.java)
                // startActivity(intent)
            } else {
                // Show an error message if username or password is empty
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


class DatabaseHelper{

    private val connectionString = "jdbc:sqlserver://statusphere-server.database.windows.net:1433;databaseName=statusphere-server/Statusphere"
    private val username = "sena"
    private val password = "your_password"

    fun authenticateUser(username: String, password: String): Boolean {
        var isAuthenticated = false
        var connection: Connection? = null

        try {
            // Establish connection
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
            connection = DriverManager.getConnection(connectionString, username, password)

            // Execute authentication query
            val query = "SELECT COUNT(*) FROM Accounts.UserAccounts WHERE Username = ? AND Password = ?"
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
            // Close connection
            connection?.close()
        }

        return isAuthenticated
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
