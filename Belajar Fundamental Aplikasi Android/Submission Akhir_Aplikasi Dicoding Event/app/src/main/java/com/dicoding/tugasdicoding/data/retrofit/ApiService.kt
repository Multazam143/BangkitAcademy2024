package com.dicoding.tugasdicoding.data.retrofit

import com.dicoding.tugasdicoding.data.response.DetailResponse
import com.dicoding.tugasdicoding.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvents(
        @Query("active") active: Int
    ): Call<EventResponse>

    @GET("events/{id}")
    fun getDetailEvent(@Path("id") id: String): Call<DetailResponse>
}