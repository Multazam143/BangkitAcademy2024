package com.dicoding.tugasdicoding.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dicoding.tugasdicoding.data.utils.AppExecutors
import com.dicoding.tugasdicoding.database.FavoriteEvent
import com.dicoding.tugasdicoding.database.FavoriteEventDao
import com.dicoding.tugasdicoding.database.FavoriteEventRoomDatabase
import com.dicoding.tugasdicoding.ui.favevent.Result

class FavoriteEventRepository(application: Application) {
    private val mFavoriteEventsDao: FavoriteEventDao
    private val appExecutors: AppExecutors = AppExecutors() // Inisialisasi AppExecutors
    private val result = MediatorLiveData<Result<List<FavoriteEvent>>>()

    init {
        val db = FavoriteEventRoomDatabase.getDatabase(application)
        mFavoriteEventsDao = db.favoriteEventDao()
    }

    fun getAllFavorites(): LiveData<Result<List<FavoriteEvent>>> {
        result.value = Result.Loading // Set loading state

        // Ambil data dari database
        val localData = mFavoriteEventsDao.getAllFavorites()
        result.addSource(localData) { favoriteEvents ->
            if (favoriteEvents != null) {
                result.value = Result.Success(favoriteEvents) // Set success state
            } else {
                result.value = Result.Error("No favorite events found") // Set error state if needed
            }
        }
        return result // Kembalikan LiveData yang berisi hasil
    }

    fun insert(favoriteEvent: FavoriteEvent) {
        appExecutors.diskIO.execute {
            mFavoriteEventsDao.insert(favoriteEvent)
        }
    }

    fun delete(favoriteEvent: FavoriteEvent) {
        appExecutors.diskIO.execute {
            mFavoriteEventsDao.delete(favoriteEvent)
        }
    }

    fun update(favoriteEvent: FavoriteEvent) {
        appExecutors.diskIO.execute {
            mFavoriteEventsDao.update(favoriteEvent)
        }
    }

    fun getFavoriteById(eventId: String): LiveData<FavoriteEvent?> {
        return mFavoriteEventsDao.getFavoriteById(eventId) // Pastikan DAO Anda memiliki metode ini
    }

    // Tambahkan metode baru untuk menghapus berdasarkan ID
    fun deleteById(eventId: String) {
        appExecutors.diskIO.execute {
            mFavoriteEventsDao.deleteById(eventId)
        }
        }

    }


