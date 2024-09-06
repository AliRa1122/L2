package com.example.app.views

import android.os.Bundle
import android.util.Log
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
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.toolbar)

        val keypass = intent.getStringExtra("KEYPASS") ?: run {
            Toast.makeText(this, "Invalid keypass", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupViewModel()
        setupRecyclerView()
        observeViewModel()

        viewModel.fetchDashboard(keypass)
    }

    private fun setupViewModel() {
        val repository = DashboardRepository(APIService.getService())
        val factory = DashboardViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[DashboardViewModel::class.java]
    }

    private fun setupRecyclerView() {
        adapter = DashboardAdapter { entity ->
            navigateToDetailsScreen(entity)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.dashboardData.observe(this) { entities ->
            Log.d("DashboardActivitys", "Received ${entities.size} entities")
            adapter.updateEntities(entities)
        }
    }

    private fun navigateToDetailsScreen(entity: Entity) {
        // TODO: Implement navigation to Details screen
        Toast.makeText(this, "Clicked on: ${entity.species}", Toast.LENGTH_SHORT).show()
    }
}