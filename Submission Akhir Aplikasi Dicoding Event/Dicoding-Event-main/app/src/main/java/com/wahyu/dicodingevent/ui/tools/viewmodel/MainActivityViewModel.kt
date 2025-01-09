package com.wahyu.dicodingevent.ui.tools.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wahyu.dicodingevent.data.SettingPreferences
import com.wahyu.dicodingevent.database.repository.EventRepository
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val repository: EventRepository,
    private val settingPreferences: SettingPreferences
) : ViewModel() {

    fun getThemeSetting(): LiveData<Boolean> {
        return settingPreferences.getThemeSetting().asLiveData()
    }

    fun getListEvent() = repository.getListEvent()

    fun saveFirstInstall() {
        viewModelScope.launch {
            settingPreferences.saveFirstInstall()
        }
    }

    fun getFirstInstall() = settingPreferences.getFirstInstall().asLiveData()
}