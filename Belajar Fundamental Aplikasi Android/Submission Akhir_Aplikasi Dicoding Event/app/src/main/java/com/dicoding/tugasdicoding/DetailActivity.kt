package com.dicoding.tugasdicoding

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.tugasdicoding.data.response.Event
import com.dicoding.tugasdicoding.database.FavoriteEvent
import com.dicoding.tugasdicoding.databinding.ActivityDetailBinding
import com.dicoding.tugasdicoding.ui.favevent.FavoriteEventViewModel
import com.dicoding.tugasdicoding.ui.favevent.ViewModelFactory

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var favoriteEventViewModel: FavoriteEventViewModel
    private var currentEventId: String? = null // Store current event ID
    private var isBookmarked = false // Favorite button status

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel for Detail and Favorite Event
        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        favoriteEventViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(FavoriteEventViewModel::class.java)

        // Get eventId from intent
        currentEventId = intent.getStringExtra(EXTRA_EVENT_ID)
        if (currentEventId != null) {
            binding.progressBar.visibility = View.VISIBLE

            // Fetch event details
            detailViewModel.fetchDetail(currentEventId!!)

            // Observe changes in event detail
            detailViewModel.detail.observe(this) { detailResponse ->
                binding.progressBar.visibility = View.GONE
                detailResponse?.event?.let { event ->
                    bindDetail(event)
                    checkIfFavorite(event.id.toString()) // Check if the event is favorite
                }
            }

            // Set action when favorite button is pressed
            binding.fab.setOnClickListener {
                val event = detailViewModel.detail.value?.event
                if (event != null) {
                    isBookmarked = !isBookmarked // Toggle status
                    updateFavoriteButton()

                    // Create favorite event object
                    val favoriteEvent = FavoriteEvent(
                        id = event.id.toString(),
                        name = event.name,
                        mediaCover = event.imageLogo,
                        isBookmarked = isBookmarked // Pass the favorite status
                    )

                    if (isBookmarked) {
                        favoriteEventViewModel.addFavoriteEvent(favoriteEvent)
                    } else {
                        favoriteEventViewModel.removeFavoriteEvent(favoriteEvent)
                    }

                    // Show message based on favorite status
                    val message = if (isBookmarked) " Add to Favourite " else " Delete from Favourites "
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                    // Return result to fragment
                    setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(EXTRA_EVENT_ID, event.id.toString())
                        putExtra(EXTRA_EVENT_REMOVED, !isBookmarked)
                    })

                    // Finish the activity and return to fragment
                    finish()
                }
            }
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    // Function to check if the event is in favorites
    private fun checkIfFavorite(eventId: String) {
        favoriteEventViewModel.favoriteEvents.observe(this) { result: com.dicoding.tugasdicoding.ui.
        favevent.Result<List<FavoriteEvent>>->
            when (result) {
                is com.dicoding.tugasdicoding.ui.favevent.Result.Success -> {
                    val isEventFavorite = result.data.any { it.id == eventId }
                    isBookmarked = isEventFavorite
                    updateFavoriteButton()
                }
                is com.dicoding.tugasdicoding.ui.favevent.Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is com.dicoding.tugasdicoding.ui.favevent.Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Error occurred: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Function to update favorite button appearance
    private fun updateFavoriteButton() {
        if (isBookmarked) {
            binding.fab.setImageResource(R.drawable.ic_favoritefull_24) // Change to filled drawable
        } else {
            binding.fab.setImageResource(R.drawable.ic_favoritenull_24) // Change to empty drawable
        }
    }

    private fun bindDetail(event: Event) {
        binding.tvEventTitle.text = getString(R.string.event_title, event.name)
        binding.tvOwnerName.text = getString(R.string.owner_name, event.ownerName)
        binding.tvBeginTime.text = getString(R.string.begin_time, event.beginTime)
        binding.tvQuota.text = getString(R.string.quota, event.quota - event.registrants)
        binding.tvDescription.text = Html.fromHtml(event.description, Html.FROM_HTML_MODE_LEGACY)

        Glide.with(this).load(event.imageLogo).into(binding.imageLogo)

        binding.btnOpenLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
            startActivity(intent)
        }
    }

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
        const val EXTRA_EVENT_REMOVED = "extra_event_removed"
        const val EXTRA_UPDATED_EVENT = "extra_updated_event" // Make sure this is defined
    }
}
