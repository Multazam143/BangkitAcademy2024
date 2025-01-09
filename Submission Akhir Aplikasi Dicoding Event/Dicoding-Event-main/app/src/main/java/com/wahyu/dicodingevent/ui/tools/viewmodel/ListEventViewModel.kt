package com.wahyu.dicodingevent.ui.tools.viewmodel

import androidx.lifecycle.ViewModel
import com.wahyu.dicodingevent.database.repository.EventRepository

class ListEventViewModel(private val repository: EventRepository) : ViewModel() {

    fun getActiveEvent() = repository.showActiveEvent()

    fun getCompletedEvent() = repository.showCompletedEvent()
}