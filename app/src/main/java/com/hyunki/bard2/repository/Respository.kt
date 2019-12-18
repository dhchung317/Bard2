package com.hyunki.bard2.repository

import com.hyunki.bard2.model.Song

interface Repository {
    fun addSong(song: Song)
    fun getSong(songTitle: String): Song
    fun deleteSong(song: Song)
}