package com.wahyu.dicodingevent.database.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.wahyu.dicodingevent.data.Result
import com.wahyu.dicodingevent.database.dao.EventDao
import com.wahyu.dicodingevent.database.entity.EventEntity
import com.wahyu.dicodingevent.response.ListEventResponse
import com.wahyu.dicodingevent.retrofit.ApiService
import com.wahyu.dicodingevent.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors
) {

    fun getListEvent(): LiveData<Result<List<EventEntity>>> {
        appExecutors.diskIO.execute {
            eventDao.delete()
        }

        val result = MediatorLiveData<Result<List<EventEntity>>>()
        result.value = Result.Loading

        val activeData = getActiveListEvent()
        val completedData = getCompletedListEvent()

        var activeLoaded = false
        var completedLoaded = false

        result.addSource(activeData) { activeResult ->
            if (activeResult is Result.Success) {
                activeLoaded = true
                if (completedLoaded) {
                    result.value = Result.Success(activeResult.data + (completedData.value as Result.Success).data)
                }
            } else if (activeResult is Result.Error) {
                result.value = activeResult
            }
        }

        result.addSource(completedData) { completedResult ->
            if (completedResult is Result.Success) {
                completedLoaded = true
                if (activeLoaded) {
                    result.value = Result.Success((activeData.value as Result.Success).data + completedResult.data)
                }
            } else if (completedResult is Result.Error) {
                result.value = completedResult
            }
        }

        return result
    }

    private fun getActiveListEvent(active: Int = 1): LiveData<Result<List<EventEntity>>> {
        val result = MediatorLiveData<Result<List<EventEntity>>>()
        result.value = Result.Loading
        val client = apiService.getEvents(active)
        client.enqueue(object : Callback<ListEventResponse> {
            override fun onResponse(p0: Call<ListEventResponse>, p1: Response<ListEventResponse>) {
                if (p1.isSuccessful) {
                    val responseBody = p1.body()?.listEvents
                    val eventList = ArrayList<EventEntity>()
                    appExecutors.diskIO.execute {
                        responseBody?.forEach { event ->
                            val listEvent = EventEntity(
                                event.id,
                                event.mediaCover,
                                event.imageLogo,
                                event.category,
                                event.name,
                                event.ownerName,
                                event.summary,
                                event.description,
                                event.quota,
                                event.registrants,
                                event.beginTime,
                                event.endTime,
                                event.link,
                                isActive = 1
                            )
                            eventList.add(listEvent)
                        }
                        eventDao.insert(eventList)
                        result.postValue(Result.Success(eventList))
                    }
                } else {
                    result.value = Result.Error("Gagal mendapatkan data event ")
                }
            }

            override fun onFailure(p0: Call<ListEventResponse>, p1: Throwable) {
                result.value = Result.Error(p1.message.toString())
            }
        })
        return result
    }

    private fun getCompletedListEvent(active: Int = 0): LiveData<Result<List<EventEntity>>> {
        val result = MediatorLiveData<Result<List<EventEntity>>>()
        result.value = Result.Loading
        val client = apiService.getEvents(active)
        client.enqueue(object : Callback<ListEventResponse> {
            override fun onResponse(p0: Call<ListEventResponse>, p1: Response<ListEventResponse>) {
                if (p1.isSuccessful) {
                    val responseBody = p1.body()?.listEvents
                    val eventList = ArrayList<EventEntity>()
                    appExecutors.diskIO.execute {
                        responseBody?.forEach { event ->
                            val listEvent = EventEntity(
                                event.id,
                                event.mediaCover,
                                event.imageLogo,
                                event.category,
                                event.name,
                                event.ownerName,
                                event.summary,
                                event.description,
                                event.quota,
                                event.registrants,
                                event.beginTime,
                                event.endTime,
                                event.link,
                                isActive = 0
                            )
                            eventList.add(listEvent)
                        }
                        eventDao.insert(eventList)
                        result.postValue(Result.Success(eventList))
                    }
                }
            }

            override fun onFailure(p0: Call<ListEventResponse>, p1: Throwable) {
                result.value = Result.Error(p1.message.toString())
            }
        })
        return result
    }

    fun showActiveEvent() = eventDao.getListActiveEvent()

    fun showCompletedEvent() = eventDao.getListCompletedEvent()

    fun getDetailEvent(eventId: Int): LiveData<Result<EventEntity>> {
        val result = MediatorLiveData<Result<EventEntity>>()
        result.value = Result.Loading
        val localData = eventDao.getDetailEvent(eventId)
        result.addSource(localData) { newData ->
            result.value = Result.Success(newData)
        }
        return result
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
            appExecutors: AppExecutors
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao, appExecutors)
            }.also { instance = it }
    }
}