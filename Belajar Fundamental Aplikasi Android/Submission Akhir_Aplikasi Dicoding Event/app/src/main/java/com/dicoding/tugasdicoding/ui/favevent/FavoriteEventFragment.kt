package com.dicoding.tugasdicoding.ui.favevent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.tugasdicoding.DetailActivity
import com.dicoding.tugasdicoding.database.FavoriteEvent
import com.dicoding.tugasdicoding.databinding.FragmentFavoriteEventBinding
import com.dicoding.tugasdicoding.ui.FavoriteEventAdapter

class FavoriteEventFragment : Fragment() {

    private var _binding: FragmentFavoriteEventBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteEventViewModel: FavoriteEventViewModel
    private lateinit var favoriteEventAdapter: FavoriteEventAdapter

    // Define the ActivityResultLauncher
    private val detailActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            data?.let {
                val eventId = it.getStringExtra(DetailActivity.EXTRA_EVENT_ID)
                val isRemoved = it.getBooleanExtra(DetailActivity.EXTRA_EVENT_REMOVED, false)
                if (isRemoved) {
                    eventId?.let { id ->
                        // Remove the event from the database
                        favoriteEventViewModel.removeFavoriteEventById(id)
                        // Remove the event from the adapter
                        favoriteEventAdapter.removeEventById(id)
                    }
                } else {
                    // If the event is not removed, check for updates
                    val updatedEvent = it.getParcelableExtra<FavoriteEvent>(DetailActivity.EXTRA_UPDATED_EVENT)
                    updatedEvent?.let { event ->
                        favoriteEventViewModel.updateFavoriteEvent(event) // Update the event if it was modified
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        favoriteEventViewModel = ViewModelProvider(this, factory).get(FavoriteEventViewModel::class.java)

        // Initialize the adapter
        favoriteEventAdapter = FavoriteEventAdapter { favoriteEvent ->
            // Navigate to DetailActivity on item click using the new launcher
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_EVENT_ID, favoriteEvent.id)
            }
            detailActivityResultLauncher.launch(intent) // Use the launcher to start the activity
        }

        // Set the adapter and layout manager
        binding.rvFavorite.apply {
            adapter = favoriteEventAdapter
            layoutManager = LinearLayoutManager(context)
        }

        // Observe favorite events
        observeFavoriteEvents()
    }

    override fun onResume() {
        super.onResume()
        // Ensure data is updated every time the fragment is displayed
        observeFavoriteEvents()
    }

    private fun observeFavoriteEvents() {
        favoriteEventViewModel.favoriteEvents.observe(viewLifecycleOwner) { result: Result<List<FavoriteEvent>> -> // Specify the type explicitly
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE // Show loading progress bar
                    binding.favoriteName.visibility = View.GONE // Hide empty message
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE // Hide progress bar
                    val favoriteEvents = result.data
                    if (favoriteEvents.isEmpty()) {
                        binding.favoriteName.visibility = View.VISIBLE // Show empty message if no favorites
                    } else {
                        binding.favoriteName.visibility = View.GONE // Hide empty message
                        favoriteEventAdapter.submitList(favoriteEvents) // Update adapter with data
                    }
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE // Hide progress bar
                    binding.favoriteName.visibility = View.GONE // Hide empty message
                    Toast.makeText(context, "Error occurred: ${result.error}", Toast.LENGTH_SHORT).show() // Show error message
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

