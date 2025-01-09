package com.wahyu.dicodingevent.ui.tools.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.wahyu.dicodingevent.data.SettingPreferences
import com.wahyu.dicodingevent.ui.tools.viewmodel.SettingsViewModel


class SettingsViewModelFactory(private val pref: SettingPreferences) : NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}