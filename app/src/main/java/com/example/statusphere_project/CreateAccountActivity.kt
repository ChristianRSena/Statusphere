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
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModel
import android.view.View
import androidx.core.content.ContextCompat




import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.regex.Matcher
import java.util.regex.Pattern




class CreateAccountActivity : AppCompatActivity() {

    private val connectionString = "jdbc:sqlserver://statusphere-server.database.windows.net:1433;databaseName=statusphere-server/Statusphere"
    private val username = "sena"
    private val password = "temp"
    private val passwordTest = "temp"
    private var color: Int = R.color.weak
    /*private val viewModel: CreateAccountViewModel by viewModels()*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)



        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextConfirmPassword = findViewById<EditText>(R.id.editTextConfirmPassword)
        /*viewModel.textViewPasswordStrength = findViewById(R.id.textViewPasswordStrength)*/

        val passwordStrengthCalculator = PasswordStrengthCalculator()

        editTextPassword.addTextChangedListener(passwordStrengthCalculator)
        passwordStrengthCalculator.strengthLevel.observe(this, Observer{strengthLevel ->
            displayStrengthLevel(strengthLevel)
        })
        passwordStrengthCalculator.strengthColor.observe(this, Observer{strengthColor ->
            color = strengthColor
        })






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
    private fun displayStrengthLevel(strengthLevel: String) {
        val createAccountButton: Button = findViewById(R.id.buttonCreateAccount)
        val strength_level_txt: TextView =findViewById(R.id.strength_level_txt)
        val strength_level_indicator: View = findViewById(R.id.strength_level_indicator)
        createAccountButton.isEnabled = strengthLevel.contains("BULLETPROOF")

        strength_level_txt.text = strengthLevel
        strength_level_txt.setTextColor(ContextCompat.getColor(this, color))
        strength_level_indicator.setBackgroundColor(ContextCompat.getColor(this, color))

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

class PasswordStrengthCalculator: TextWatcher {

    val strengthLevel: MutableLiveData<String> = MutableLiveData()
    val strengthColor: MutableLiveData<Int> = MutableLiveData()

    var lowerCase: MutableLiveData<Int> = MutableLiveData(0)
    var upperCase: MutableLiveData<Int> = MutableLiveData(0)
    var digit: MutableLiveData<Int> = MutableLiveData(0)
    var specialChar: MutableLiveData<Int> = MutableLiveData(0)

    override fun beforeTextChanged(char: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(char: CharSequence?, start: Int, before: Int, count: Int) {
        if (char != null){
            lowerCase.value = if (char.hasLowerCase()) { 1 } else { 0 }
            upperCase.value = if (char.hasUpperCase()) { 1 } else { 0 }
            digit.value = if (char.hasDigit()) { 1 } else { 0 }
            specialChar.value = if (char.hasSpecialChar()) { 1 } else { 0 }
            calculateStrength(char)

        }




    }
    override fun afterTextChanged(char: Editable?) {}

    private fun calculateStrength(password: CharSequence) {
        if(password.length in 0..7){
            strengthColor.value = R.color.weak
            strengthLevel.value = "WEAK"
        }else if(password.length in 8..10){
            if(lowerCase.value == 1 || upperCase.value == 1 || digit.value == 1 || specialChar.value == 1){
                strengthColor.value = R.color.medium
                strengthLevel.value = "MEDIUM"
            }
        }else if(password.length in 11..16){
            if(lowerCase.value == 1 || upperCase.value == 1 || digit.value == 1 || specialChar.value == 1){
                if(lowerCase.value == 1 && upperCase.value == 1){
                    strengthColor.value = R.color.strong
                    strengthLevel.value = "STRONG"
                }
            }
        }else if(password.length > 16){
            if(lowerCase.value == 1 && upperCase.value == 1 && digit.value == 1 && specialChar.value == 1){
                strengthColor.value = R.color.bulletproof
                strengthLevel.value = "BULLETPROOF"
            }
        }
    }


    private fun CharSequence.hasLowerCase(): Boolean{
        val pattern: Pattern = Pattern.compile("[a-z]")
        val hasLowerCase: Matcher = pattern.matcher(this)
        return hasLowerCase.find()
    }
    private fun CharSequence.hasUpperCase(): Boolean{
        val pattern: Pattern = Pattern.compile("[A-Z]")
        val hasUpperCase: Matcher = pattern.matcher(this)
        return hasUpperCase.find()
    }
    private fun CharSequence.hasDigit(): Boolean{
        val pattern: Pattern = Pattern.compile("[0-9]")
        val hasDigit: Matcher = pattern.matcher(this)
        return hasDigit.find()
    }
    private fun CharSequence.hasSpecialChar(): Boolean{
        val pattern: Pattern = Pattern.compile("[!@#$%^&*()_+=-{}.<>|\\[\\]~]")
        val hasSpecialChar: Matcher = pattern.matcher(this)
        return hasSpecialChar.find()
    }
    enum class StrengthLevel {
        WEAK,
        MEDIUM,
        STRONG,
        BULLETPROOF
    }



}



/*class CreateAccountViewModel : ViewModel() {
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
*/


