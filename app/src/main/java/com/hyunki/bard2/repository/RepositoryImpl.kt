package com.hyunki.bard2.repository

import android.app.Application

import com.hyunki.bard2.database.Database
import com.hyunki.bard2.model.Song

import androidx.lifecycle.LiveData

class RepositoryImpl(application: Application) : Repository {
    private val database: Database = Database(application)

    val songList: LiveData<List<Song>>
        get() = database.allSongs

    override fun addSong(song: Song) {
        database.addSong(song)
    }

    override fun getSong(songTitle: String) : Song {
        return database.getSong(songTitle)
    }

    override fun deleteSong(song: Song?) {
        database.deleteSong(song!!.songTitle)
    }
}
