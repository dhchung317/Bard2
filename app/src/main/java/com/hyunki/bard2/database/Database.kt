package com.hyunki.bard2.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.annotation.Nullable

import com.hyunki.bard2.model.Note
import com.hyunki.bard2.model.Song

import java.util.ArrayList
import androidx.lifecycle.MutableLiveData

class Database(@Nullable context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, SCHEMA_VERSION) {

    val allSongs: MutableLiveData<List<Song>>
        get() {
            val titles = ArrayList<String>()
            val cursor = getReadableDatabase().rawQuery(
                    "SELECT * FROM $TABLE_PARENT;",
                    null)
            if (cursor != null) {
                if (cursor!!.moveToFirst()) {
                    do {
                        titles.add(cursor!!.getString(cursor!!.getColumnIndex("song_name")))
                    } while (cursor!!.moveToNext())
                }
            }
            val liveData = MutableLiveData<List<Song>>()
            val returnList = ArrayList<Song>()
            for (s in titles) {
                val childCursor = getReadableDatabase().rawQuery(
                        "SELECT * FROM $TABLE_CHILD WHERE song_name = '$s';", null)
                val song = Song(s)
                var note: Note? = null
                if (childCursor != null) {
                    if (childCursor!!.moveToFirst()) {
                        do {
                            note = Note(
                                    childCursor!!.getInt(childCursor!!.getColumnIndex("raw_note")),
                                    childCursor!!.getString(childCursor!!.getColumnIndex("note_syllable")),
                                    childCursor!!.getInt(childCursor!!.getColumnIndex("note_duration")),
                                    childCursor!!.getString(childCursor!!.getColumnIndex("note_name"))
                            )
                            song.addNote(note)
                        } while (childCursor!!.moveToNext())
                    }
                }
                returnList.add(song)
            }
            liveData.setValue(returnList)
            return liveData
        }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
                "CREATE TABLE " + TABLE_PARENT +
                        " (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "song_name TEXT UNIQUE);")
        db.execSQL(
                "CREATE TABLE " + TABLE_CHILD +
                        " (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "raw_note INTEGER," +
                        "note_syllable TEXT," +
                        "note_duration Integer," +
                        "note_name TEXT," +
                        "song_name TEXT," +
                        "FOREIGN KEY(song_name) REFERENCES TABLE_PARENT(song_name));")
    }

    fun addSong(song: Song) {
        val cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_PARENT + " WHERE song_name = '" + song.songTitle +
                        "';", null)

        if (cursor.getCount() === 0) {
            getWritableDatabase().execSQL("INSERT INTO " + TABLE_PARENT +
                    "(song_name) VALUES('" + song.songTitle + "')")

            for (note in song.getSongNotes()!!) {
                getWritableDatabase().execSQL("INSERT INTO " + TABLE_CHILD +
                        "(raw_note, note_syllable, note_duration, note_name, song_name) " +
                        "VALUES('" + note.rawNote + "', '" + note.syllable + "', '"
                        + note.duration + "','" + note.note
                        + "', '" + song.songTitle + "');")
            }
        }
        cursor.close()
    }

    fun getSong(songName: String): Song {
        val song = Song(songName)
        var note: Note? = null
        val checker = getReadableDatabase().rawQuery(
                "SELECT * FROM $TABLE_PARENT WHERE song_name = '$songName';", null)
        val cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM $TABLE_CHILD WHERE song_name = '$songName';", null)
        if (checker.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    note = Note(
                            cursor.getInt(cursor.getColumnIndex("raw_note")),
                            cursor.getString(cursor.getColumnIndex("note_syllable")),
                            cursor.getInt(cursor.getColumnIndex("note_duration")),
                            cursor.getString(cursor.getColumnIndex("note_name")))
                    song.addNote(note)
                } while (cursor.moveToNext())
            }
        }
        checker.close()
        cursor.close()
        return song
    }

    fun deleteSong(songTitle: String) {
        val cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM $TABLE_PARENT WHERE song_name = '$songTitle';", null)
        val childCursor = getReadableDatabase().rawQuery(
                "SELECT * FROM $TABLE_CHILD WHERE song_name = '$songTitle';", null)
        if (cursor != null) {
            getWritableDatabase().execSQL(
                    "DELETE FROM $TABLE_PARENT WHERE song_name = '$songTitle';")
        }
        do {
            getWritableDatabase().execSQL(
                    "DELETE FROM $TABLE_CHILD WHERE song_name = '$songTitle';")
        } while (childCursor.moveToNext())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    companion object {

        private val TABLE_PARENT = "Songs"
        private val TABLE_CHILD = "Notes"
        private val DATABASE_NAME = "songs.db"
        private val SCHEMA_VERSION = 1
    }
}
