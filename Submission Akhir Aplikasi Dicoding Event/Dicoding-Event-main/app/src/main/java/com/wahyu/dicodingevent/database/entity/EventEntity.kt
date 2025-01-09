package com.wahyu.dicodingevent.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "data_event")
@Parcelize
data class EventEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val imgCover: String,
    val imgEvent: String,
    val category: String,
    val name: String,
    val ownerName: String,
    val summaryEvent: String,
    val description: String,
    val quota: Int,
    val registrants: Int,
    val beginTime: String,
    val endTime: String,
    val link: String,
    var isActive: Int
) : Parcelable