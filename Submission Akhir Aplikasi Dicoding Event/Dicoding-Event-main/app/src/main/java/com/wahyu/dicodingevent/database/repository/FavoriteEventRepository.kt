package com.wahyu.dicodingevent.database.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.wahyu.dicodingevent.database.dao.EventDao
import com.wahyu.dicodingevent.database.dao.FavoriteEventDao
import com.wahyu.dicodingevent.database.entity.FavoriteEventEntity
import com.wahyu.dicodingevent.utils.AppExecutors

class FavoriteEventRepository private constructor(
    private val eventDao: EventDao,
    private val favoriteDao: FavoriteEventDao,
    private val appExecutors: AppExecutors
) {

    fun getFavoriteEvent() = favoriteDao.getFavoriteEvent()

    fun isFavoriteEvent(eventId: Int): LiveData<Boolean> {
        return (favoriteDao.getFavoriteEventById(eventId)).map { favoriteEvent ->
            favoriteEvent != null
        }
    }


    fun setFavoriteEvent(eventId: Int) {
        appExecutors.diskIO.execute {
            val event = eventDao.getEventId(eventId)
            event?.let {
                val favoriteEvent = FavoriteEventEntity(
                    event.id,
                    event.imgCover,
                    event.imgEvent,
                    event.category,
                    event.name,
                    event.ownerName,
                    event.summaryEvent,
                    event.description,
                    event.quota,
                    event.registrants,
                    event.beginTime,
                    event.endTime,
                    event.link,
                    event.isActive
                )
                favoriteDao.insert(favoriteEvent)
            }
        }
    }

    fun deleteFavoriteEvent(id: Int) {
        appExecutors.diskIO.execute {
            favoriteDao.deleteFavoriteEvent(id)
        }
    }

    companion object {

        @Volatile
        private var instance: FavoriteEventRepository? = null
        fun getInstance(
            eventDao: EventDao,
            favoriteDao: FavoriteEventDao,
            appExecutors: AppExecutors
        ): FavoriteEventRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteEventRepository(eventDao, favoriteDao, appExecutors)
            }
    }
}