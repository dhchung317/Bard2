package com.hyunki.bard2

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.fragment.app.FragmentActivity

import com.hyunki.bard2.model.Note
import com.hyunki.bard2.model.Song

import java.util.HashMap
import java.util.Timer
import java.util.TimerTask

class SongPlayer(private val context: FragmentActivity?, private val tts: TextToSpeech) {
    private val params by lazy { HashMap<String, String>() }
    internal var mp: MediaPlayer = MediaPlayer()
    private lateinit var playlist: List<Note>
    private var i = 0
    internal var timer: Timer = Timer()
    private var started = false

    init {
        params[TextToSpeech.Engine.KEY_PARAM_VOLUME] = "0.1"
        tts.setSpeechRate(.3.toFloat())
    }

    fun playSong(song: Song) {
        playlist = song.songNotes

        if (playlist.isNotEmpty() && mp.isPlaying) {
            Toast.makeText(context, "media player is playing", Toast.LENGTH_SHORT).show()
        } else if (playlist.isEmpty()) {
            Toast.makeText(context, "your song is empty!", Toast.LENGTH_SHORT).show()
        } else {
            if (playlist.isNotEmpty() && i < playlist.size) {
                mp = MediaPlayer.create(context, playlist[0].rawNote)
                playAll(playlist[0])
            }
        }
    }

    fun vocalize(n: Note) {
        tts.setPitch(calculatePitch(n))
        tts.speak(n.syllable, TextToSpeech.QUEUE_FLUSH, params)
    }

    fun playAll(currentNote: Note) {
        timer.schedule(object : TimerTask() {
            override fun run() {
                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    mp.reset()
                    if (!started) {
                        mp = MediaPlayer.create(context, playlist[i].rawNote)
                        mp.seekTo(600)
                        mp.start()
                        vocalize(playlist[i])
                        playAll(playlist[i])
                        started = true
                    } else if (i < playlist.size - 1) {
                        mp = MediaPlayer.create(context, playlist[++i].rawNote)
                        mp.seekTo(600)
                        mp.start()
                        vocalize(playlist[i])
                        if (i != playlist.size - 1) {
                            playAll(playlist[i])
                        }
                    }
                }
            }
        }, currentNote.duration.toLong())

        if (i == playlist.size - 1) {
            mp.stop()
            started = false
            timer.cancel()
            i = 0
            timer = Timer()
        }
    }

    private fun calculatePitch(n: Note): Float = when (n.note) {
        "Aflat3" -> 1.7.toFloat()
        "A3" -> 1.8.toFloat()
        "Bflat3" -> 1.9.toFloat()
        "B3" -> 2.toFloat()
        "C3" -> 1.toFloat()
        "Dflat3" -> 1.1.toFloat()
        "D3" -> 1.2.toFloat()
        "Eflat3" -> 1.3.toFloat()
        "E3" -> 1.4.toFloat()
        "F3" -> 1.5.toFloat()
        "Gflat3" -> 1.6.toFloat()
        "G3" -> 1.7.toFloat()
        else -> 1.toFloat()
    }
}
