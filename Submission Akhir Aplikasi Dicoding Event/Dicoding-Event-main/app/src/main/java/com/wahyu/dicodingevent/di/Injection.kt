package com.wahyu.dicodingevent.di

import android.content.Context
import com.wahyu.dicodingevent.data.SettingPreferences
import com.wahyu.dicodingevent.data.dataStore
import com.wahyu.dicodingevent.database.EventRoomDatabase
import com.wahyu.dicodingevent.database.repository.EventRepository
import com.wahyu.dicodingevent.database.repository.FavoriteEventRepository
import com.wahyu.dicodingevent.retrofit.ApiConfig
import com.wahyu.dicodingevent.ui.tools.factory.DetailActivityVMFactory
import com.wahyu.dicodingevent.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventRoomDatabase.getInstance(context)
        val eventDao = database.eventDao()
        val appExecutors = AppExecutors()
        return EventRepository.getInstance(apiService, eventDao, appExecutors)
    }

    fun provideFavoriteRepository(context: Context): FavoriteEventRepository {
        val database = EventRoomDatabase.getInstance(context)
        val eventDao = database.eventDao()
        val favoriteDao = database.favoriteEventDao()
        val appExecutors = AppExecutors()
        return FavoriteEventRepository.getInstance(eventDao, favoriteDao, appExecutors)
    }

    fun provideDetailRepository(context: Context) : DetailActivityVMFactory {
        val eventRepository = provideRepository(context)
        val favoriteEventRepository = provideFavoriteRepository(context)
        return DetailActivityVMFactory(eventRepository, favoriteEventRepository)
    }

    fun provideSettingPreferences(context: Context): SettingPreferences {
        return SettingPreferences.getInstance(context.dataStore)
    }
}