package com.wahyu.dicodingevent.ui.tools.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wahyu.dicodingevent.database.repository.EventRepository
import com.wahyu.dicodingevent.di.Injection
import com.wahyu.dicodingevent.ui.tools.viewmodel.ListEventViewModel


class EventViewModelFactory(private val repository: EventRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        modelClass.isAssignableFrom(ListEventViewModel::class.java)
        return ListEventViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        @Volatile
        private var instance: EventViewModelFactory? = null
        fun getInstance(context: Context): EventViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: EventViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}