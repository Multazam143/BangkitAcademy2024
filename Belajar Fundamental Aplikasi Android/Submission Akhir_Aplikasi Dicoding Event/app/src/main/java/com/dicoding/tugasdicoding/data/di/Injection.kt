package com.dicoding.tugasdicoding.data.di

import android.app.Application
import android.content.Context
import com.dicoding.tugasdicoding.data.repository.FavoriteEventRepository

object Injection {
    fun provideFavoriteEventRepository(context: Context): FavoriteEventRepository {
        // Pastikan untuk menggunakan context sebagai Application
        val application = context.applicationContext as Application
        return FavoriteEventRepository(application)
    }
}

