package com.example.app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.views.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge UI for modern immersive design
        enableEdgeToEdge()

        // Set the content view to the activity_main layout
        setContentView(R.layout.activity_main)

        // Handle window insets to adjust padding for system bars (status/navigation bars)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            // Apply padding to the view based on the system bars' inset values
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Post a delayed task to navigate to LoginActivity after 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            // Create an intent to launch LoginActivity
            val intent = Intent(this, LoginActivity::class.java)

            // Start LoginActivity
            startActivity(intent)

            // Finish MainActivity so that the user cannot return to this splash screen
            finish()
        }, 2000) // Delay of 2 seconds (2000 milliseconds)
    }
}
