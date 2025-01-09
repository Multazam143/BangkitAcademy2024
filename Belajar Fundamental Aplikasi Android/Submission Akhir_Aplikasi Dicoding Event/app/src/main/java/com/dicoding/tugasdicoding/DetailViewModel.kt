package com.dicoding.tugasdicoding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.tugasdicoding.data.response.DetailResponse
import com.dicoding.tugasdicoding.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _detail = MutableLiveData<DetailResponse>()
    val detail: LiveData<DetailResponse> get() = _detail

    fun fetchDetail(eventId: String) {
        val apiService = ApiConfig.getApiService()
        apiService.getDetailEvent(eventId).enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                if (response.isSuccessful) {
                    _detail.value = response.body()
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }
}
