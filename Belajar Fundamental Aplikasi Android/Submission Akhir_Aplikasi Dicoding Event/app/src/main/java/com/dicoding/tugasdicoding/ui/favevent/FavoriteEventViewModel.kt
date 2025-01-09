package com.dicoding.tugasdicoding.ui.favevent
import com.dicoding.tugasdicoding.database.FavoriteEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.tugasdicoding.data.repository.FavoriteEventRepository
import kotlinx.coroutines.launch

class FavoriteEventViewModel(private val repository: FavoriteEventRepository) : ViewModel() {
    // LiveData untuk menampung list event favorit
    val favoriteEvents: LiveData<Result<List<FavoriteEvent>>> = repository.getAllFavorites()

    // Method untuk menambahkan event ke dalam daftar favorit
    fun addFavoriteEvent(favoriteEvent: FavoriteEvent) {
        viewModelScope.launch {
            repository.insert(favoriteEvent)
        }
    }

    // Method untuk menghapus event dari daftar favorit
    fun removeFavoriteEvent(favoriteEvent: FavoriteEvent) {
        viewModelScope.launch {
            repository.delete(favoriteEvent) // Panggil metode delete
        }
    }

    fun updateFavoriteEvent(favoriteEvent: FavoriteEvent) {
        viewModelScope.launch {
            repository.update(favoriteEvent)
        }
    }

    // Method untuk menghapus event dari daftar favorit berdasarkan ID
    fun removeFavoriteEventById(eventId: String) {
        viewModelScope.launch {
            repository.deleteById(eventId)
        }
    }
}



