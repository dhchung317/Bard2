package com.hyunki.bard2

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.widget.Toast

import com.hyunki.bard2.model.Note
import com.hyunki.bard2.model.Song

import java.util.HashMap
import java.util.Timer
import java.util.TimerTask

class SongPlayer(private val context: Context, private val tts: TextToSpeech) {
    private val params = HashMap()
    var mp: MediaPlayer? = null
        private set
    private var playlist: List<Note>? = null
    private var i = 0
    private var timer: Timer? = null
    private var started = false

    init {
        params.put(TextToSpeech.Engine.KEY_PARAM_VOLUME, "0.1")
        tts.setSpeechRate(.3.toFloat())
        timer = Timer()
        mp = MediaPlayer()
    }

    fun playSong(song: Song) {
        playlist = song.getSongNotes()

        if (!playlist!!.isEmpty() && mp!!.isPlaying()) {
            Toast.makeText(context, "media player is playing", Toast.LENGTH_SHORT).show()
        } else if (playlist!!.isEmpty()) {
            Toast.makeText(context, "your song is empty!", Toast.LENGTH_SHORT).show()
        } else {
            if (playlist!!.size() >= 1 && i < playlist!!.size()) {
                mp = MediaPlayer.create(context, playlist!![0].getRawNote())
                playAll(playlist!![0])
            }
        }
    }

    fun vocalize(n: Note) {
        tts.setPitch(calculatePitch(n))
        tts.speak(n.getSyllable(), TextToSpeech.QUEUE_FLUSH, params)
    }

    fun playAll(currentNote: Note) {
        timer!!.schedule(object : TimerTask() {
            @Override
            fun run() {
                val handler = Handler(Looper.getMainLooper())
                handler.post({
                    mp!!.reset()
                    if (!started) {
                        mp = MediaPlayer.create(context, playlist!![i].getRawNote())
                        mp!!.seekTo(600)
                        mp!!.start()
                        vocalize(playlist!![i])
                        playAll(playlist!![i])
                        started = true
                    } else if (i < playlist!!.size() - 1) {
                        mp = MediaPlayer.create(context, playlist!![++i].getRawNote())
                        mp!!.seekTo(600)
                        mp!!.start()
                        vocalize(playlist!![i])
                        if (i != playlist!!.size() - 1) {
                            playAll(playlist!![i])
                        }
                    }
                })
            }
        }, currentNote.getDuration())

        if (i == playlist!!.size() - 1) {
            mp!!.stop()
            started = false
            timer!!.cancel()
            i = 0
            timer = Timer()
        }
    }

    fun calculatePitch(n: Note): Float {
        when (n.getNote()) {
            "Aflat3" -> return 1.7.toFloat()
            "A3" -> return 1.8.toFloat()
            "Bflat3" -> return 1.9.toFloat()
            "B3" -> return 2.toFloat()
            "C3" -> return 1.toFloat()
            "Dflat3" -> return 1.1.toFloat()
            "D3" -> return 1.2.toFloat()
            "Eflat3" -> return 1.3.toFloat()
            "E3" -> return 1.4.toFloat()
            "F3" -> return 1.5.toFloat()
            "Gflat3" -> return 1.6.toFloat()
            "G3" -> return 1.7.toFloat()
            else -> return 1.toFloat()
        }
    }
}
