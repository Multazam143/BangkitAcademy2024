package com.wahyu.dicodingevent.ui.tools.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wahyu.dicodingevent.R
import com.wahyu.dicodingevent.database.entity.FavoriteEventEntity
import com.wahyu.dicodingevent.databinding.ItemEventBinding
import com.wahyu.dicodingevent.ui.DetailEventActivity
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FavoriteEventAdapter : ListAdapter<FavoriteEventEntity, FavoriteEventAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.fetchListEvent(event)
    }

    class MyViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        private var isEventClosed = false

        init {
            binding.root.setOnClickListener {
                val context = it.context
                val intent = Intent(context, DetailEventActivity::class.java)
                intent.putExtra("EVENT_ID", getId.id)
                context.startActivity(intent)
            }
        }

        private lateinit var getId: FavoriteEventEntity

        @SuppressLint("SetTextI18n")
        fun fetchListEvent(data: FavoriteEventEntity) {
            getId = data
            isEventClosed = isEventClosed(data)
            Glide.with(binding.imgEvent.context)
                .load(data.imgEvent)
                .placeholder(R.drawable.img_placeholdedr_logo)
                .error(R.drawable.img_placeholdedr_logo)
                .fallback(R.drawable.img_placeholdedr_logo)
                .fitCenter()
                .into(binding.imgEvent)
            binding.tvCategoryEvent.text = data.category
            binding.tvNameEvent.text = data.name
            binding.tvOwnerNameEvent.text = data.ownerName
            binding.tvSummaryEvent.text = data.summaryEvent
            val remainingQuota = data.quota - data.registrants
            if (isEventClosed) {
                binding.tvLeftQuota.text = "Kuota Ditutup"
            } else {
                if (remainingQuota == 0) {
                    binding.tvLeftQuota.text = "Waiting List"
                } else {
                    binding.tvLeftQuota.text = "Sisa Kuota: $remainingQuota"
                }
            }
            val timeRemaining = calculateRemainingTime(data.beginTime)
            binding.timeLeft.text = timeRemaining
        }

        private fun calculateRemainingTime(beginTime: String): String {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val start = LocalDateTime.parse(beginTime, formatter)
            val now = LocalDateTime.now()

            val duration = Duration.between(now, start)

            return if (duration.isNegative) {
                "Event Sudah Selesai"
            } else if (duration.toDays() > 0) {
                "${duration.toDays()} hari tersisa"
            } else if (duration.toHours() > 0) {
                "${duration.toHours()} jam tersisa"
            } else {
                "${duration.toMinutes()} menit tersisa"
            }
        }

        private fun isEventClosed(data: FavoriteEventEntity): Boolean {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val start = LocalDateTime.parse(data.beginTime, formatter)
            val now = LocalDateTime.now()

            val duration = Duration.between(now, start)
            return duration.isNegative

        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteEventEntity>() {
            override fun areItemsTheSame(
                oldItem: FavoriteEventEntity,
                newItem: FavoriteEventEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: FavoriteEventEntity,
                newItem: FavoriteEventEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}