package com.hyunki.bard2.viewmodel

import android.app.Application

import com.hyunki.bard2.model.ClickableNote
import com.hyunki.bard2.model.Song
import com.hyunki.bard2.repository.RepositoryImpl

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ViewModel(application: Application) : AndroidViewModel(application) {
    
    lateinit var currentNote: ClickableNote

    val allSongs: LiveData<List<Song>>

    private val repositoryImpl = RepositoryImpl(application)

    init {
        this.allSongs = repositoryImpl.songList
    }

    fun addSong(song: Song) {
        repositoryImpl.addSong(song)
    }

    fun getSong(songTitle: String): Song {
        return repositoryImpl.getSong(songTitle)
    }

    fun deleteSong(song: Song) {
        repositoryImpl.deleteSong(song)
    }

    fun getRepository():RepositoryImpl{
        return repositoryImpl
    }
}
