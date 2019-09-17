package com.hyunki.bard2.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

import java.util.ArrayList

@Parcelize
data class Song(
        var songTitle: String = "",
        var songNotes:ArrayList<Note> = ArrayList()

):Parcelable{

    fun addNote(note: Note) {
        songNotes.add(note)
    }
    fun deleteNote() {
        songNotes.removeAt(songNotes.size - 1)
    }
    fun getSongNotes(): List<Note>? {
        return songNotes
    }
}
