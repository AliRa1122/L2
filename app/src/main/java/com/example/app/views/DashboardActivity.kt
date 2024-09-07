package com.example.app.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.R
import com.example.app.adapters.DashboardAdapter
import com.example.app.databinding.ActivityDashboardBinding
import com.example.app.models.Entity
import com.example.app.repositories.DashboardRepository
import com.example.app.utils.APIService
import com.example.app.viewmodels.DashboardViewModel
import com.example.app.viewmodels.DashboardViewModelFactory

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var viewModel: DashboardViewModel
    private lateinit var adapter: DashboardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using ViewBinding
        binding = ActivityDashboardBinding.inflate(layoutInflater)

        // Enable edge-to-edge UI for a seamless layout
        enableEdgeToEdge()

        // Set the content view to the root view from the binding
        setContentView(binding.root)

        // Handle window insets to adjust padding for system bars (status/navigation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set the toolbar as the support ActionBar
        setSupportActionBar(binding.toolbar)

        // Retrieve the keypass from the Intent extras
        val keypass = intent.getStringExtra("KEYPASS") ?: run {
            // Show a Toast and finish the activity if keypass is invalid or missing
            Toast.makeText(this, "Invalid keypass", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Set up the ViewModel for the dashboard screen
        setupViewModel()

        // Set up the RecyclerView for displaying the list of entities
        setupRecyclerView()

        // Observe changes in the ViewModel's LiveData properties
        observeViewModel()

        // Fetch the dashboard data using the provided keypass
        viewModel.fetchDashboard(keypass)
    }

    // Method to set up the ViewModel using the ViewModelFactory
    private fun setupViewModel() {
        // Create a repository instance that interacts with the API
        val repository = DashboardRepository(APIService.getService())

        // Use the ViewModelFactory to create the DashboardViewModel
        val factory = DashboardViewModelFactory(repository)

        // Initialize the ViewModel
        viewModel = ViewModelProvider(this, factory)[DashboardViewModel::class.java]
    }

    // Method to set up the RecyclerView with an adapter and layout manager
    private fun setupRecyclerView() {
        // Initialize the adapter and define the click listener for the RecyclerView items
        adapter = DashboardAdapter { entity ->
            navigateToDetailsScreen(entity)
        }

        // Set a LinearLayoutManager for the RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Set the adapter to the RecyclerView
        binding.recyclerView.adapter = adapter
    }

    // Method to observe LiveData from the ViewModel and update the UI accordingly
    private fun observeViewModel() {
        // Observe the loading state and show/hide the ProgressBar
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        }

        // Observe any error messages and display them as Toast notifications
        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        // Observe changes to the list of entities and update the adapter's data
        viewModel.dashboardData.observe(this) { entities ->
            adapter.updateEntities(entities)
        }
    }

    // Method to navigate to the details screen for a selected entity
    private fun navigateToDetailsScreen(entity: Entity) {
        // Display a Toast message showing which entity was clicked
        Toast.makeText(this, "Clicked on: ${entity.species}", Toast.LENGTH_SHORT).show()

        // Create an Intent to start the DetailsActivity
        val intent = Intent(this, DetailsActivity::class.java)

        // Pass the selected entity to the DetailsActivity via the Intent
        intent.putExtra("ENTITY", entity)

        // Start the DetailsActivity
        startActivity(intent)
    }
}
