package com.wahyu.dicodingevent.ui.tools.viewmodel

import androidx.lifecycle.ViewModel
import com.wahyu.dicodingevent.database.repository.FavoriteEventRepository

class FavoriteEventViewModel(private val repository: FavoriteEventRepository) : ViewModel() {

    fun getFavoriteEvent() = repository.getFavoriteEvent()
}