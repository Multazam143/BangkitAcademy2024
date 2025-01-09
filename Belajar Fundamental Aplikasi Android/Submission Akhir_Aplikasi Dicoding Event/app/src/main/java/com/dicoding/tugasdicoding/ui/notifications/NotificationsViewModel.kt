package com.dicoding.tugasdicoding.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.tugasdicoding.data.response.EventResponse
import com.dicoding.tugasdicoding.data.response.ListEventsItem
import com.dicoding.tugasdicoding.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsViewModel : ViewModel() {
    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> get() = _events

    init {
        fetchEvents()
    }

    private fun fetchEvents() {
        val apiService = ApiConfig.getApiService()
        apiService.getEvents(1).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    _events.value = response.body()?.listEvents ?: listOf()
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }
}
