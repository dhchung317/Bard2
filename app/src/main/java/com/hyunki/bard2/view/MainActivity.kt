package com.hyunki.bard2.view

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

import android.os.Bundle
import android.view.View
import android.view.animation.Animation

import com.hyunki.bard2.Animations
import com.hyunki.bard2.R
import com.hyunki.bard2.controller.ClickableNoteListener
import com.hyunki.bard2.model.ClickableNote
import com.hyunki.bard2.viewmodel.ViewModel
import com.hyunki.bard2.controller.FragmentInteractionListener
import com.hyunki.bard2.databinding.ActivityMainBinding
import com.hyunki.bard2.model.Song

class MainActivity : AppCompatActivity(), FragmentInteractionListener, ClickableNoteListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ViewModel
    private lateinit var drop: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
        drop = Animations.getDropImageAnimation(
                binding.mainActivitySplashImageView)
        drop.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) = Unit
            override fun onAnimationEnd(animation: Animation) {
                binding.mainActivitySplashImageView.visibility = View.INVISIBLE
                supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, MainFragment.newInstance())
                        .commit()
            }

            override fun onAnimationRepeat(animation: Animation) = Unit
        })
        binding.mainActivitySplashImageView.startAnimation(drop)
    }

    override fun displaySong(song: Song) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, SongFragment.newInstance(song))
                .addToBackStack(SONG_FRAGMENT_KEY)
                .commit()
    }

    override fun displayComposer() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, ComposeFragment.newInstance())
                .addToBackStack(COMPOSE_FRAGMENT_KEY)
                .commit()
    }

    override fun displayLibrary() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, LibraryFragment.newInstance())
                .addToBackStack(LIBRARY_FRAGMENT_KEY)
                .commit()
    }

    override fun setCurrentNote(note: ClickableNote) {
        viewModel.currentNote = note
    }

    companion object {
        const val SONG_FRAGMENT_KEY = "displaySong"
        const val COMPOSE_FRAGMENT_KEY = "composeSong"
        const val LIBRARY_FRAGMENT_KEY = "displayLibrary"
    }
}
