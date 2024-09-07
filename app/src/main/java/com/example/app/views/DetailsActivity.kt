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
    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val entity = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("ENTITY", Entity::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("ENTITY") as? Entity
        }
        entity?.let { displayEntityDetails(it) }

    }

    @SuppressLint("SetTextI18n")
    private fun displayEntityDetails(entity: Entity) {
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

    override fun onSupportNavigateUp(): Boolean {
        return true
    }
}