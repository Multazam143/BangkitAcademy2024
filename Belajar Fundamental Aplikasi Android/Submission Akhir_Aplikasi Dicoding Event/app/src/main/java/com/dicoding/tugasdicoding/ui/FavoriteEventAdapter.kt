package com.dicoding.tugasdicoding.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.tugasdicoding.database.FavoriteEvent
import com.dicoding.tugasdicoding.databinding.ItemFavoriteBinding

class FavoriteEventAdapter(
    private val onItemClick: (FavoriteEvent) -> Unit
) : ListAdapter<FavoriteEvent, FavoriteEventAdapter.FavoriteEventViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEventViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteEventViewHolder, position: Int) {
        val favoriteEvent = getItem(position)
        holder.bind(favoriteEvent)
    }

    inner class FavoriteEventViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoriteEvent: FavoriteEvent) {
            binding.tveventName.text = favoriteEvent.name
            Glide.with(binding.root.context)
                .load(favoriteEvent.mediaCover)
                .into(binding.imageCover)

            // Handle item click
            binding.root.setOnClickListener {
                onItemClick(favoriteEvent) // Pass the clicked item to the callback
            }
        }
    }

    // Method to remove an event by ID
    fun removeEventById(eventId: String) {
        // Get the current list of items
        val currentList = currentList.toMutableList()
        // Find the item to remove
        val itemToRemove = currentList.find { it.id == eventId }
        if (itemToRemove != null) {
            currentList.remove(itemToRemove)
            // Submit the updated list to the adapter
            submitList(currentList)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<FavoriteEvent> =
            object : DiffUtil.ItemCallback<FavoriteEvent>() {
                override fun areItemsTheSame(oldItem: FavoriteEvent, newItem: FavoriteEvent): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: FavoriteEvent, newItem: FavoriteEvent): Boolean {
                    return oldItem == newItem
                }
            }
    }
}

