package com.wahyu.dicodingevent.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.wahyu.dicodingevent.R
import com.wahyu.dicodingevent.database.entity.EventEntity
import com.wahyu.dicodingevent.databinding.ActivityDetailBinding
import com.wahyu.dicodingevent.ui.tools.factory.DetailActivityVMFactory
import com.wahyu.dicodingevent.ui.tools.viewmodel.DetailEventViewModel
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DetailEventActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailEventViewModel: DetailEventViewModel
    private var isFavoriteStatus: Boolean = false

    companion object {
        private const val TAG = "LogDetailEventActivity"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val factory: DetailActivityVMFactory = DetailActivityVMFactory.getInstance(this)
        detailEventViewModel = ViewModelProvider(this, factory)[DetailEventViewModel::class.java]

        detailEventViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val eventId = intent.getIntExtra("EVENT_ID", 0)
        Log.d(TAG, "Received EVENT_ID: $eventId")

        detailEventViewModel.isFavoriteEvent(eventId)
        detailEventViewModel.isFavoriteEvent(eventId).observe(this) { isFavorite ->
            isFavoriteStatus = isFavorite
            updateFavoriteIcon(isFavorite)
            Log.d(TAG, "isFavorite value: $isFavorite")
        }

        detailEventViewModel.getDetailEvent(eventId)
        detailEventViewModel.detailEvent.observe(this) { eventDetail ->
            Log.d(TAG, "Event Detail: $eventDetail")
            eventDetail?.let {
                with(binding) {
                    Glide.with(this@DetailEventActivity)
                        .load(it.imgCover)
                        .placeholder(R.drawable.img_placeholder_cover)
                        .error(R.drawable.img_placeholder_cover)
                        .fallback(R.drawable.img_placeholder_cover)
                        .into(imgCover)
                    cardFavorite.setOnClickListener {
                        if (isFavoriteStatus) {
                            detailEventViewModel.deleteFavoriteEvent(eventId)
                        } else {
                            detailEventViewModel.setFavoriteEvent(eventId)
                        }
                    }
                    Log.d(TAG, "isFavoriteStatus value: $isFavoriteStatus")
                    tvCategoryEvent.text = it.category
                    tvNameEvent.text = it.name
                    tvOwnerNameEvent.text = it.ownerName
                    timeStart.text = it.beginTime
                    timeEnd.text = it.endTime
                    btnSend.setOnClickListener { _ ->
                        val url = it.link
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(url)
                        startActivity(intent)
                    }
                    val remainingQuota = it.quota - it.registrants

                    val isEventClosed = isEventClosed(it)
                    if (isEventClosed) {
                        tvLeftQuota.text = "Kuota Ditutup"
                    } else {
                        if (remainingQuota == 0) {
                            tvLeftQuota.text = "Waiting List"
                        } else {
                            tvLeftQuota.text = "Sisa Kuota: $remainingQuota"
                        }
                    }
                    tvTextDescription.text = HtmlCompat.fromHtml(
                        it.description, HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                }
            }
        }

        detailEventViewModel.message.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun isEventClosed(data: EventEntity): Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val start = LocalDateTime.parse(data.beginTime, formatter)
        val now = LocalDateTime.now()

        val duration = Duration.between(now, start)
        return duration.isNegative
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val icon = if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        Glide.with(this)
            .load(icon)
            .into(binding.imgFavorite)
    }

}