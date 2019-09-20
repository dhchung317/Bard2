package com.hyunki.bard2.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.annotation.Nullable

import com.hyunki.bard2.model.Note
import com.hyunki.bard2.model.Song

import java.util.ArrayList
import androidx.lifecycle.MutableLiveData

class Database(@Nullable context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, SCHEMA_VERSION) {
    val allSongs: MutableLiveData<List<Song>>
        get() {
            val titles = ArrayList<String>()
            val cursor = readableDatabase.rawQuery(
                    "SELECT * FROM $TABLE_PARENT;",
                    null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        titles.add(cursor.getString(cursor.getColumnIndex("song_name")))
                    } while (cursor.moveToNext())
                }
            }
            val liveData = MutableLiveData<List<Song>>()
            val returnList = ArrayList<Song>()
            for (s in titles) {
                val childCursor = readableDatabase.rawQuery(
                        "SELECT * FROM $TABLE_CHILD WHERE song_name = '$s';", null)
                val notes: MutableList<Note> = ArrayList()
                if (childCursor != null) {
                    if (childCursor.moveToFirst()) {
                        do {
                            val note = Note(
                                    rawNote = childCursor.getInt(childCursor.getColumnIndex("raw_note")),
                                    syllable = childCursor.getString(childCursor.getColumnIndex("note_syllable")),
                                    duration = childCursor.getInt(childCursor.getColumnIndex("note_duration")),
                                    note = childCursor.getString(childCursor.getColumnIndex("note_name"))
                            )
                            notes.add(note)
                        } while (childCursor.moveToNext())
                    }
                }
                returnList.add(Song(s, notes))
                childCursor.close()
            }
            liveData.value = returnList
            cursor.close()
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
        val cursor = readableDatabase.rawQuery(
                "SELECT * FROM " + TABLE_PARENT + " WHERE song_name = '" + song.songTitle +
                        "';", null)
        if (cursor.count == 0) {
            writableDatabase.execSQL("INSERT INTO " + TABLE_PARENT +
                    "(song_name) VALUES('" + song.songTitle + "')")
            for (note in song.songNotes) {
                writableDatabase.execSQL("INSERT INTO " + TABLE_CHILD +
                        "(raw_note,note_syllable,note_duration,note_name,song_name) " +
                        "VALUES('" + note.rawNote + "', '" + note.syllable + "', '"
                        + note.duration + "','" + note.note
                        + "', '" + song.songTitle + "');")
            }
        }
        cursor.close()
    }

    fun getSong(songName: String): Song {
        val notes: MutableList<Note> = ArrayList()
        val checker = readableDatabase.rawQuery(
                "SELECT * FROM $TABLE_PARENT WHERE song_name = '$songName';", null)
        val cursor = readableDatabase.rawQuery(
                "SELECT * FROM $TABLE_CHILD WHERE song_name = '$songName';", null)
        if (checker.count > 0) {
            if (cursor.moveToFirst()) {
                do {
                    notes.add(Note(
                            cursor.getInt(cursor.getColumnIndex("raw_note")),
                            cursor.getString(cursor.getColumnIndex("note_syllable")),
                            cursor.getInt(cursor.getColumnIndex("note_duration")),
                            cursor.getString(cursor.getColumnIndex("note_name")))
                    )
                } while (cursor.moveToNext())
            }
        } else {
            return Song("", notes)
        }
        checker.close()
        cursor.close()
        return Song(songName, notes)
    }

    fun deleteSong(songTitle: String) {
        val cursor = readableDatabase.rawQuery(
                "SELECT * FROM $TABLE_PARENT WHERE song_name = '$songTitle';", null)
        val childCursor = readableDatabase.rawQuery(
                "SELECT * FROM $TABLE_CHILD WHERE song_name = '$songTitle';", null)
        if (cursor != null) {
            writableDatabase.execSQL(
                    "DELETE FROM $TABLE_PARENT WHERE song_name = '$songTitle';")
        }
        do {
            writableDatabase.execSQL(
                    "DELETE FROM $TABLE_CHILD WHERE song_name = '$songTitle';")
        } while (childCursor.moveToNext())
        cursor.close()
        childCursor.close()
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {
        private const val TABLE_PARENT = "Songs"
        private const val TABLE_CHILD = "Notes"
        private const val DATABASE_NAME = "songs.db"
        private const val SCHEMA_VERSION = 1
    }
}
