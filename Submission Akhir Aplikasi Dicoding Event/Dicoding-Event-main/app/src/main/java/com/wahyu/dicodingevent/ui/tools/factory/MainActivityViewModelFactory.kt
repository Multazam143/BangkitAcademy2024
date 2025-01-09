package com.wahyu.dicodingevent.ui.tools.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wahyu.dicodingevent.data.SettingPreferences
import com.wahyu.dicodingevent.database.repository.EventRepository
import com.wahyu.dicodingevent.di.Injection
import com.wahyu.dicodingevent.ui.tools.viewmodel.MainActivityViewModel

class MainActivityViewModelFactory(
    private val repository: EventRepository,
    private val settingPreferences: SettingPreferences
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        modelClass.isAssignableFrom(MainActivityViewModel::class.java)
        return MainActivityViewModel(repository, settingPreferences) as T
    }

    companion object {
        @Volatile
        private var instance: MainActivityViewModelFactory? = null
        fun getInstance(context: Context): MainActivityViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: MainActivityViewModelFactory(
                    Injection.provideRepository(context),
                    Injection.provideSettingPreferences(context)
                )
            }.also { instance = it }
    }
}