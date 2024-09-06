package com.example.app.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app.databinding.ItemDashboardEntityBinding
import com.example.app.models.Entity

class DashboardAdapter(private val onItemClick: (Entity) -> Unit) : RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {

    private var entities: List<Entity> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun updateEntities(newEntities: List<Entity>) {
        Log.d("DashboardAdapter", "Updating entities, new size: ${newEntities.size}")
        entities = newEntities
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("DashboardAdapter", "Creating new ViewHolder")
        val binding = ItemDashboardEntityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("DashboardAdapter", "Binding ViewHolder at position $position")
        holder.bind(entities[position])
    }

    override fun getItemCount(): Int = entities.size

    inner class ViewHolder(private val binding: ItemDashboardEntityBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(entity: Entity) {
            Log.d("DashboardAdapter", "Binding entity: ${entity.species}")
            binding.speciesTextView.text = entity.species
            binding.scientificNameTextView.text = entity.scientificName
            binding.habitatTextView.text = "Habitat: ${entity.habitat}"
            binding.dietTextView.text = "Diet: ${entity.diet}"
            binding.conservationStatusTextView.text = "Status: ${entity.conservationStatus}"
            binding.averageLifespanTextView.text = "Avg. Lifespan: ${entity.averageLifespan} years"

            binding.root.setOnClickListener { onItemClick(entity) }
        }
    }
}