package com.hyunki.bard2.viewmodel

import android.app.Application

import com.hyunki.bard2.model.ClickableNote
import com.hyunki.bard2.model.Song
import com.hyunki.bard2.repository.Repository

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository

    var currentNote: ClickableNote? = null

    val allSongs: LiveData<List<Song>>

    init {
        this.repository = Repository.getRepositoryInstance(application)
        this.allSongs = repository.getSongList()
    }

    fun addSong(song: Song) {
        repository.addSong(song)
    }

    fun getSong(songTitle: String): Song {
        return repository.getSong(songTitle)
    }

    fun deleteSong(song: Song) {
        repository.deleteSong(song)
    }
}
