package com.example.menuseafood
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuSeafood (
    val name : String,
    val description: String,
    val detail: String,
    val photo: Int)

    : Parcelable