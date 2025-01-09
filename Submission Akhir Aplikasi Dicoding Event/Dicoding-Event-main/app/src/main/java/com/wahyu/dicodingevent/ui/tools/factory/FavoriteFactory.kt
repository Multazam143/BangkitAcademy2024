package com.wahyu.dicodingevent.ui.tools.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wahyu.dicodingevent.database.repository.FavoriteEventRepository
import com.wahyu.dicodingevent.di.Injection
import com.wahyu.dicodingevent.ui.tools.viewmodel.FavoriteEventViewModel


class FavoriteFactory(private val repository: FavoriteEventRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        modelClass.isAssignableFrom(FavoriteEventViewModel::class.java)
        return FavoriteEventViewModel(repository) as T
        throw IllegalArgumentException("Kelas ViewModel Tidak diketahui")
    }

    companion object {
        @Volatile
        private var instance: FavoriteFactory? = null
        fun getInstance(context: Context): FavoriteFactory =
            instance ?: synchronized(this) {
                instance ?: FavoriteFactory(Injection.provideFavoriteRepository(context))
            }.also { instance = it }
    }
}