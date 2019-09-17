package com.hyunki.bard2.repository

import android.app.Application

import com.hyunki.bard2.database.Database
import com.hyunki.bard2.model.Song

import androidx.lifecycle.LiveData

class Repository private constructor(application: Application) {
    private val database: Database
    val songList: LiveData<List<Song>>
        get() = database.getAllSongs()

    init {
        database = Database(application)
    }

    fun addSong(song: Song) {
        database.addSong(song)
    }

    fun getSong(songTitle: String): Song {
        return database.getSong(songTitle)
    }

    fun deleteSong(song: Song) {
        database.deleteSong(song.getSongTitle())
    }

    companion object {
        private var repositoryInstance: Repository? = null

        fun getRepositoryInstance(application: Application): Repository {
            if (repositoryInstance == null) {
                repositoryInstance = Repository(application)
            }
            return repositoryInstance
        }
    }
}
