import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.statusphere_project.R

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
