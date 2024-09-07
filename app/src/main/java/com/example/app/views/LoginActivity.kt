package com.example.app.views

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.InputType
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.app.R
import com.example.app.databinding.ActivityLoginBinding
import com.example.app.repositories.AuthRepository
import com.example.app.utils.APIService
import com.example.app.viewmodels.AuthFactory
import com.example.app.viewmodels.LoginActivityViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge layout for modern immersive UI
        enableEdgeToEdge()

        // Inflate the layout using view binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Adjust the padding for system bars (status/navigation bars)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the ViewModel with the factory pattern
        viewModel = ViewModelProvider(
            this,
            AuthFactory(AuthRepository(APIService.getService()), application)
        )[LoginActivityViewModel::class.java]

        // Set up observers for LiveData
        setupObservers()

        // Toggle password visibility button
        val passToggle = binding.passVisibilityToggle

        // Toggle between showing and hiding the password input field
        passToggle.setOnClickListener {
            val isPasswordVisible = binding.passwordEditText.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            // Switch input type between visible and hidden password
            binding.passwordEditText.inputType = if (isPasswordVisible) {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            } else {
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }
            // Set the cursor to the end of the text
            binding.passwordEditText.setSelection(binding.passwordEditText.text.length)
            // Change icon based on visibility state
            passToggle.setBackgroundResource(
                if (isPasswordVisible) R.drawable.ic_eye_close else R.drawable.ic_eye_open
            )
        }

        // Handle login button click
        binding.loginButton.setOnClickListener {
            hideKeyboard() // Hide the keyboard when the login button is clicked

            // Check if the device is connected to the internet
            if (!isNetworkAvailable()) {
                Toast.makeText(this, "No Network Connection", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Get username and password input
            var email = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            // Trim white space from username
            val trimmedEmail = email.trim()
            binding.usernameEditText.setText(trimmedEmail)
            email = trimmedEmail

            // Validate that the username is not empty
            if (trimmedEmail.isEmpty()) {
                binding.usernameEditText.error = "Username is required"
                return@setOnClickListener
            }

            // Validate that the password is not empty
            if (password.isEmpty()) {
                binding.passwordEditText.error = "Password is required"
                return@setOnClickListener
            }

            // Proceed with login if both fields are filled
            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.loginUser(email, password)
            }
        }

        // Register button click event (currently not implemented)
        binding.registerBtn.setOnClickListener {
            Toast.makeText(this, "Not Yet Implemented", Toast.LENGTH_SHORT).show()
        }

        // Forget password click event (currently not implemented)
        binding.forgetPasswordLabel.setOnClickListener {
            Toast.makeText(this, "Not Yet Implemented", Toast.LENGTH_SHORT).show()
        }
    }

    // Called when login is successful
    private fun onLoginSuccess(keypass: String) {
        // Navigate to the Dashboard Activity and pass the keypass
        navigateToDashboardActivity(keypass)
    }

    // Set up LiveData observers for loading state, error message, and successful login keypass
    private fun setupObservers() {
        viewModel.getIsLoading().observe(this) { isLoading ->
            // Show or hide the progress bar based on loading state
            binding.progressBar.isVisible = isLoading
        }

        viewModel.getErrorMessage().observe(this) { errorMessage ->
            // Show error message in an alert dialog if login fails
            if (errorMessage!!.isNotEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Login Failed")
                    .setMessage(errorMessage)
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }

        viewModel.getKeyPass().observe(this) { keypass ->
            // If login is successful and keypass is not null, proceed to the dashboard
            if (keypass != null) {
                onLoginSuccess(keypass)
            }
        }
    }

    // Navigate to DashboardActivity, passing the keypass as an extra
    private fun navigateToDashboardActivity(keypass: String) {
        Toast.makeText(this, "KeyPass: $keypass", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra("KEYPASS", keypass)
        startActivity(intent)
        finish() // Finish the login activity to prevent returning to it with the back button
    }

    // Hide the soft keyboard when no longer needed
    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.loginButton.windowToken, 0)
    }

    // Check if the device is connected to a network (Wi-Fi, Cellular, Ethernet, or Bluetooth)
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        // Check for various types of network transports (Wi-Fi, Cellular, etc.)
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }
}
