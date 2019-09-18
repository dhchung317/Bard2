package com.hyunki.bard2.view

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable

import com.hyunki.bard2.R
import com.hyunki.bard2.SongPlayer
import com.hyunki.bard2.controller.FragmentInteractionListener
import com.hyunki.bard2.databinding.FragmentSongBinding
import com.hyunki.bard2.viewmodel.ViewModel
import com.hyunki.bard2.model.Song

class SongFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentSongBinding
    private lateinit var listener: FragmentInteractionListener
    private lateinit var viewModel: ViewModel
    private lateinit var tts: TextToSpeech
    private lateinit var player: SongPlayer
    private lateinit var song: Song

   override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
    }

    override fun onAttach(@NonNull context: Context) {
        super.onAttach(context)
        if (context is FragmentInteractionListener) {
            listener = context
        }
    }

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSongBinding.inflate(inflater,container,false)
        tts = TextToSpeech(activity) {}
        player = SongPlayer(activity, tts)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var displayNotesString: String = ""
        val args = arguments
        if (args != null) {
            song = args.getParcelable(SONG_FRAGMENT_BUNDLE_KEY)!!
        }

        for (n in song.songNotes) {
            if (displayNotesString == "") {
                displayNotesString = "${n.note} "
            } else {
                displayNotesString += "${n.note} "
            }
        }

        binding.songFragmentDisplayNotesTextview.text = displayNotesString
        binding.songFragmentSongTitleTextview.text = song.songTitle

        binding.songFragmentDeleteButton.setOnClickListener(this)
        binding.songFragmentExitButton.setOnClickListener(this)
        binding.songFragmentPlayButton.setOnClickListener(this)
    }

    private fun deleteSong(song: Song) {
        viewModel.deleteSong(song)
        Toast.makeText(activity, getString(R.string.song_deleted_message), Toast.LENGTH_SHORT).show()
        assert(fragmentManager != null)
        fragmentManager?.popBackStack(MainActivity.LIBRARY_FRAGMENT_KEY, 1)
        fragmentManager?.popBackStack(MainActivity.SONG_FRAGMENT_KEY, 0)
        listener.displayLibrary()
    }

    private fun exitSongFragment() {
        activity?.onBackPressed()
    }

    private fun playSong(song: Song) {

        player.playSong(viewModel.getSong(song.songTitle))
        while (player.mp.isPlaying) {
            binding.songFragmentPlayButton.isEnabled = false
        }
        binding.songFragmentPlayButton.isEnabled = true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.songFragment_play_button -> playSong(song)
            R.id.songFragment_delete_button -> deleteSong(song)
            R.id.songFragment_exit_button -> exitSongFragment()
        }
    }

    override fun onDestroy() {
        tts.shutdown()
        player.mp.release()
        player.timer.cancel()
        super.onDestroy()
    }

    companion object {
        private const val SONG_FRAGMENT_BUNDLE_KEY = "song"

        fun newInstance(song: Song): SongFragment {
            val bundle = Bundle()
            bundle.putParcelable(SONG_FRAGMENT_BUNDLE_KEY, song)
            val fragment = SongFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}

