package com.wahyu.dicodingevent.ui.tools.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.wahyu.dicodingevent.data.Result
import com.wahyu.dicodingevent.database.entity.EventEntity
import com.wahyu.dicodingevent.database.repository.EventRepository
import com.wahyu.dicodingevent.database.repository.FavoriteEventRepository

class DetailEventViewModel(
    private val eventRepository: EventRepository,
    private val favoriteEventRepository: FavoriteEventRepository
) : ViewModel() {

    private val _detailEvent = MutableLiveData<EventEntity>()
    val detailEvent: LiveData<EventEntity> = _detailEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val message: LiveData<String> = _errorMessage

    companion object {
        private const val TAG = "DetailEventViewModel"
    }

    fun getDetailEvent(eventId: Int) {
        _isLoading.value = true
        val detailEvent = eventRepository.getDetailEvent(eventId)
        detailEvent.observeForever { result ->
            when (result) {
                is Result.Success -> {
                    _isLoading.value = false
                    _detailEvent.value = result.data
                }
                is Result.Error -> {
                    _isLoading.value = false
                    _errorMessage.value = result.error
                }
                Result.Loading -> {
                    _isLoading.value = true
                }
            }
        }
    }

    fun isFavoriteEvent(eventId: Int) : LiveData<Boolean> {
        return favoriteEventRepository.isFavoriteEvent(eventId).map { isFavoriteEvent ->
            Log.d(TAG, "Status Favorite $eventId: $isFavoriteEvent")
            isFavoriteEvent
        }
    }

    fun setFavoriteEvent(eventId: Int) {
        return favoriteEventRepository.setFavoriteEvent(eventId)
    }

    fun deleteFavoriteEvent(id: Int) {
        return favoriteEventRepository.deleteFavoriteEvent(id)
    }
}