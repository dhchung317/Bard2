package com.hyunki.bard2

import android.content.Context

import com.hyunki.bard2.database.Database
import com.hyunki.bard2.model.Song

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

import androidx.test.core.app.ApplicationProvider

@RunWith(RobolectricTestRunner::class)
class TestDatabase {
    internal var context: Context
    internal var database: Database

    @Before
    @Throws(Exception::class)
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        database = Database(context)
    }

    @Test
    fun test_database_return_test_song_title() {
        database.addSong(Song(TEST_SONG_TITLE))
        val input = TEST_SONG_TITLE
        val expected = TEST_SONG_TITLE
        val result = database.getSong(input).getSongTitle()
        Assert.assertEquals(expected, result)
    }

    companion object {

        private val TEST_SONG_TITLE = "test"
    }
}
