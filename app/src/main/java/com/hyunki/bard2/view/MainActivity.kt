package com.hyunki.bard2.view

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView

import com.hyunki.bard2.Animations
import com.hyunki.bard2.R
import com.hyunki.bard2.controller.ClickableNoteListener
import com.hyunki.bard2.model.ClickableNote
import com.hyunki.bard2.viewmodel.ViewModel
import com.hyunki.bard2.controller.FragmentInteractionListener
import com.hyunki.bard2.model.Song

class MainActivity : AppCompatActivity(), FragmentInteractionListener, ClickableNoteListener {
    private var viewModel: ViewModel? = null
    private var splash: ImageView? = null

    @Override
    protected fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
        splash = findViewById(R.id.mainActivity_splash_imageView)
        val drop = Animations.getDropImageAnimation(splash)
        drop.setAnimationListener(object : Animation.AnimationListener() {
            @Override
            fun onAnimationStart(animation: Animation) {
            }

            @Override
            fun onAnimationEnd(animation: Animation) {
                splash!!.setVisibility(View.INVISIBLE)
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_container, MainFragment.newInstance())
                        .commit()
            }

            @Override
            fun onAnimationRepeat(animation: Animation) {
            }
        })
        splash!!.startAnimation(drop)
    }

    @Override
    fun displaySong(song: Song) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, SongFragment.newInstance(song))
                .addToBackStack(SONG_FRAGMENT_KEY)
                .commit()
    }

    @Override
    fun displayComposer() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, ComposeFragment.newInstance())
                .addToBackStack(COMPOSE_FRAGMENT_KEY)
                .commit()
    }

    @Override
    fun displayLibrary() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, LibraryFragment.newInstance())
                .addToBackStack(LIBRARY_FRAGMENT_KEY)
                .commit()
    }


    @Override
    fun setCurrentNote(note: ClickableNote) {
        viewModel!!.setCurrentNote(note)
    }

    companion object {
        val SONG_FRAGMENT_KEY = "displaySong"
        val COMPOSE_FRAGMENT_KEY = "composeSong"
        val LIBRARY_FRAGMENT_KEY = "displayLibrary"
    }
}
