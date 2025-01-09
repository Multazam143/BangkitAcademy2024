package com.wahyu.dicodingevent.ui.tools.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wahyu.dicodingevent.database.repository.EventRepository
import com.wahyu.dicodingevent.database.repository.FavoriteEventRepository
import com.wahyu.dicodingevent.di.Injection
import com.wahyu.dicodingevent.ui.tools.viewmodel.DetailEventViewModel

class DetailActivityVMFactory(
    private val eventRepository: EventRepository,
    private val favoriteEventRepository: FavoriteEventRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailEventViewModel::class.java)) {
            return DetailEventViewModel(eventRepository, favoriteEventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        @Volatile
        private var instance: DetailActivityVMFactory? = null
        fun getInstance(context: Context): DetailActivityVMFactory =
            instance ?: synchronized(this) {
                instance ?: Injection.provideDetailRepository(context)
                    .also { instance = it }
            }
    }
}