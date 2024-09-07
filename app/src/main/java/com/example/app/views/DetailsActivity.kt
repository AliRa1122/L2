package com.example.app.views

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.R
import com.example.app.databinding.ActivityDetailsBinding
import com.example.app.models.Entity

class DetailsActivity : AppCompatActivity() {
    // Binding object to access the views in the activity layout
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge layout for a modern UI design
        enableEdgeToEdge()

        // Inflate the layout using ViewBinding
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle window insets to adjust padding for system bars (status/navigation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up the toolbar as the support ActionBar
        setSupportActionBar(binding.toolbar)

        // Enable the "Up" button in the ActionBar to allow navigation back
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Retrieve the entity object passed from the previous activity
        val entity = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Use the new method to get the parcelable in newer Android versions
            intent.getParcelableExtra("ENTITY", Entity::class.java)
        } else {
            // Use the deprecated method for older Android versions
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("ENTITY") as? Entity
        }

        // If the entity is not null, display its details in the UI
        entity?.let { displayEntityDetails(it) }
    }

    // Method to display the entity details in the UI
    @SuppressLint("SetTextI18n")
    private fun displayEntityDetails(entity: Entity) {
        // Use ViewBinding to populate the TextViews with entity data
        binding.apply {
            speciesTextView.text = entity.species
            scientificNameTextView.text = entity.scientificName
            habitatTextView.text = "Habitat: ${entity.habitat}"
            dietTextView.text = "Diet: ${entity.diet}"
            conservationStatusTextView.text = "Conservation Status: ${entity.conservationStatus}"
            averageLifespanTextView.text = "Average Lifespan: ${entity.averageLifespan} years"
            descriptionTextView.text = entity.description
        }
    }

    // Handle the "Up" button press to navigate back to the previous activity
    override fun onSupportNavigateUp(): Boolean {
        // Go back to the previous screen when the up button is pressed
        onBackPressed()
        return true
    }
}
