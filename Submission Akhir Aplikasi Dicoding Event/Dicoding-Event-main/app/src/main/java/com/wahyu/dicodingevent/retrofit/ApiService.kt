package com.wahyu.dicodingevent.retrofit

import com.wahyu.dicodingevent.response.ListEventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events")
    fun getEvents(
        @Query("active") active: Int
    ): Call<ListEventResponse>
}