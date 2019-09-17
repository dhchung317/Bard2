package com.hyunki.bard2.view

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import com.hyunki.bard2.R
import com.hyunki.bard2.SongPlayer
import com.hyunki.bard2.controller.FragmentInteractionListener
import com.hyunki.bard2.viewmodel.ViewModel
import com.hyunki.bard2.model.Song

import java.util.Objects

class SongFragment : Fragment(), View.OnClickListener {
    private var listener: FragmentInteractionListener? = null
    private var viewModel: ViewModel? = null
    private var tts: TextToSpeech? = null
    private var player: SongPlayer? = null
    private var song: Song? = null

    @BindView(R.id.songFragment_displayNotes_textview)
    internal var displayNotes: TextView? = null
    @BindView(R.id.songFragment_songTitle_textview)
    internal var songTitle: TextView? = null
    @BindView(R.id.songFragment_play_button)
    internal var playButton: Button? = null
    @BindView(R.id.songFragment_exit_button)
    internal var deleteButton: Button? = null
    @BindView(R.id.songFragment_delete_button)
    internal var exitButton: Button? = null

    @Override
    fun onCreate(@Nullable savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
    }

    @Override
    fun onAttach(@NonNull context: Context) {
        super.onAttach(context)
        if (context is FragmentInteractionListener) {
            listener = context as FragmentInteractionListener
        }
    }

    @Nullable
    @Override
    fun onCreateView(@NonNull inflater: LayoutInflater, @Nullable container: ViewGroup, @Nullable savedInstanceState: Bundle): View {
        val rootview = inflater.inflate(R.layout.fragment_song, container, false)
        tts = TextToSpeech(getActivity()) { status -> }
        player = SongPlayer(getActivity(), tts)
        return rootview
    }

    @Override
    fun onViewCreated(@NonNull view: View, @Nullable savedInstanceState: Bundle) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)
        var displayNotesString: String? = null
        val args = getArguments()

        assert(args != null)
        song = args!!.getParcelable(SONG_FRAGMENT_BUNDLE_KEY)

        assert(song != null)
        for (n in song!!.getSongNotes()) {
            if (displayNotesString == null) {
                displayNotesString = n.getNote() + " "
            } else {
                displayNotesString += n.getNote() + " "
            }
        }

        displayNotes!!.setText(displayNotesString)
        songTitle!!.setText(song!!.getSongTitle())
    }

    private fun deleteSong(song: Song?) {
        viewModel!!.deleteSong(song)
        Toast.makeText(getActivity(), getString(R.string.song_deleted_message), Toast.LENGTH_SHORT).show()
        assert(getFragmentManager() != null)
        getFragmentManager().popBackStack(MainActivity.LIBRARY_FRAGMENT_KEY, 1)
        getFragmentManager().popBackStack(MainActivity.SONG_FRAGMENT_KEY, 0)
        listener!!.displayLibrary()
    }

    private fun exitSongFragment() {
        Objects.requireNonNull(getActivity()).onBackPressed()
    }

    private fun playSong(song: Song) {

        player!!.playSong(viewModel!!.getSong(song.getSongTitle()))
        if (player!!.getMp() != null) {
            while (player!!.getMp().isPlaying()) {
                playButton!!.setEnabled(false)
            }
        }
        playButton!!.setEnabled(true)
    }

    @OnClick(R.id.songFragment_play_button, R.id.songFragment_delete_button, R.id.songFragment_exit_button)
    @Override
    fun onClick(v: View) {
        when (v.getId()) {
            R.id.songFragment_play_button -> playSong(song!!)
            R.id.songFragment_delete_button -> deleteSong(song)
            R.id.songFragment_exit_button -> exitSongFragment()
        }
    }

    @Override
    fun onDestroy() {
        tts!!.shutdown()
        if (player!!.getMp() != null) {
            player!!.getMp().release()
        }
        super.onDestroy()
    }

    companion object {
        private val SONG_FRAGMENT_BUNDLE_KEY = "song"


        fun newInstance(song: Song): SongFragment {
            val bundle = Bundle()
            bundle.putParcelable(SONG_FRAGMENT_BUNDLE_KEY, song)
            val fragment = SongFragment()
            fragment.setArguments(bundle)
            return fragment
        }
    }
}

