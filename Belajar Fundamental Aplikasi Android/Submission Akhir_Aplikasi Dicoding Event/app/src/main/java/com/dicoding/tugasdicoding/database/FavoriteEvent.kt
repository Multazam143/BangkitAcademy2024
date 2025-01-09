package com.dicoding.tugasdicoding.database
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class FavoriteEvent(
    @PrimaryKey val id: String, // Ensure this field is annotated as primary key
    val name: String,
    val mediaCover: String? = null,
    val isBookmarked: Boolean
) : Parcelable