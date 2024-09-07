package com.example.app.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app.databinding.ItemDashboardEntityBinding
import com.example.app.models.Entity

// Adapter class for managing and binding data to RecyclerView for displaying a list of entities
class DashboardAdapter(private val onItemClick: (Entity) -> Unit) : RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {

    // List to store entities
    private var entities: List<Entity> = emptyList()

    // Updates the list of entities and notifies the adapter to refresh the RecyclerView
    @SuppressLint("NotifyDataSetChanged")
    fun updateEntities(newEntities: List<Entity>) {
        entities = newEntities
        // Notify RecyclerView that the dataset has changed
        notifyDataSetChanged()
    }

    // Inflates the layout for each item in the RecyclerView and returns a ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the layout defined in ItemDashboardEntityBinding
        val binding = ItemDashboardEntityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Binds data to the ViewHolder for the specified position
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind the entity data to the ViewHolder
        holder.bind(entities[position])
    }

    // Returns the total number of entities to be displayed
    override fun getItemCount(): Int = entities.size

    // Inner class representing a ViewHolder for each item in the RecyclerView
    inner class ViewHolder(private val binding: ItemDashboardEntityBinding) : RecyclerView.ViewHolder(binding.root) {

        // Binds the data for a specific entity to the UI components
        @SuppressLint("SetTextI18n")
        fun bind(entity: Entity) {
            // Set the text for each TextView in the item layout based on the entity data
            binding.speciesTextView.text = entity.species
            binding.scientificNameTextView.text = entity.scientificName
            binding.habitatTextView.text = "Habitat: ${entity.habitat}"
            binding.dietTextView.text = "Diet: ${entity.diet}"
            binding.conservationStatusTextView.text = "Status: ${entity.conservationStatus}"
            binding.averageLifespanTextView.text = "Avg. Lifespan: ${entity.averageLifespan} years"

            // Set an onClickListener to handle item click events
            binding.root.setOnClickListener { onItemClick(entity) }
        }
    }
}
