package com.example.app.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
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
    private lateinit var sh:SharedPreferences
    private lateinit var editor:SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize ViewModel
        viewModel = ViewModelProvider(this,
            AuthFactory(AuthRepository(APIService.getService()), application)
        )[LoginActivityViewModel::class.java]

        setupObservers()

        val passToggle = binding.passVisibilityToggle

        // Toggle password visibility
        passToggle.setOnClickListener {
            val isPasswordVisible = binding.passwordEditText.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.passwordEditText.inputType = if (isPasswordVisible) {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            } else {
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }
            binding.passwordEditText.setSelection(binding.passwordEditText.text.length)
            // Change icon based on password visibility state
            passToggle.setBackgroundResource(
                if (isPasswordVisible) R.drawable.ic_eye_close else R.drawable.ic_eye_open
            )
        }

        // Logging In
        binding.loginButton.setOnClickListener {
            hideKeyboard()

            if(!isNetworkAvailable()) {
                Toast.makeText(this, "No Network Connection", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Get username and password
            var email = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            // Trim the username from both sides
            val trimmedEmail = email.trim()
            binding.usernameEditText.setText(trimmedEmail)
            email = trimmedEmail

            // Validate
            if(trimmedEmail.isEmpty()) {
                binding.usernameEditText.error = "Username is required"
                return@setOnClickListener
            }

            if(password.isEmpty()) {
                binding.passwordEditText.error = "Password is required"
                return@setOnClickListener
            }

            // Login
            if(email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.loginUser(email, password)
            }
        }

        // Navigation to Register Activity
        binding.registerBtn.setOnClickListener {
            // Not Yet Implemented
            Toast.makeText(this, "Not Yet Implemented", Toast.LENGTH_SHORT).show()
        }

        // Forget Password Btn
        binding.forgetPasswordLabel.setOnClickListener {
            // Not Yet Implemented
            Toast.makeText(this, "Not Yet Implemented", Toast.LENGTH_SHORT).show()
        }
    }

    // Handle login success
    private fun onLoginSuccess(keypass: String) {
        // Move to the Dashboard Activity
        navigateToDashboardActivity(keypass)
    }

    // Observers
    private fun setupObservers() {
        viewModel.getIsLoading().observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        viewModel.getErrorMessage().observe(this) { errorMessage ->
            if (errorMessage!!.isNotEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Login Failed")
                    .setMessage(errorMessage)
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }

        viewModel.getKeyPass().observe(this) { keypass ->
            if (keypass != null) {
                onLoginSuccess(keypass)
            }
        }
    }

    // Navigate to Dashboard Activity
    private fun navigateToDashboardActivity(keypass: String) {
        Toast.makeText(this, "KeyPass: $keypass", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra("KEYPASS", keypass)
        startActivity(intent)
        finish()
    }

    // Hide Keyboard
    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.loginButton.windowToken, 0)
    }

    // Checks if network is available (Internet/Wifi connection is available)
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected == true && (activeNetwork.type == ConnectivityManager.TYPE_WIFI || activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
    }

}
