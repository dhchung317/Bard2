package com.hyunki.bard2.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.hyunki.bard2.R
import com.hyunki.bard2.controller.FragmentInteractionListener
import com.hyunki.bard2.controller.NotesAdapter
import com.hyunki.bard2.model.ClickableNote
import com.hyunki.bard2.model.Note
import com.hyunki.bard2.model.Song
import com.hyunki.bard2.viewmodel.ViewModel

import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import java.util.ArrayList
import java.util.Arrays

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

class ComposeFragment : Fragment(), View.OnClickListener {
    private var listener: FragmentInteractionListener? = null
    private var viewModel: ViewModel? = null
    private var song: Song? = null
    private var rawId: Int = 0
    private var noteName: String? = null
    private var defaultDuration: String? = null
    private var adapter: NotesAdapter? = null
    private val clickableNotes = ArrayList()

    @BindView(R.id.composeFragment_songTitle_editText)
    internal var songTitle: EditText? = null
    @BindView(R.id.composeFragment_syllable_editText)
    internal var syllable: EditText? = null
    @BindView(R.id.composeFragment_duration_editText)
    internal var durationInput: EditText? = null
    @BindView(R.id.composeFragment_displayCurrentNotes_textView)
    internal var currentNotes: TextView? = null
    //    @BindView(R.id.composeFragment_notes_spinner)
    //    Spinner notes;
    @BindView(R.id.compose_recyclerview)
    internal var noteRecycler: RecyclerView? = null
    @BindView(R.id.composeFragment_addNote_button)
    internal var addNotes: Button? = null
    @BindView(R.id.composeFragment_deleteNote_button)
    internal var deleteNotes: Button? = null
    @BindView(R.id.composeFragment_addSong_button)
    internal var addSong: Button? = null
    @BindView(R.id.composeFragment_library_button)
    internal var gotoLibrary: Button? = null

    @Override
    fun onCreate(@Nullable savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        defaultDuration = "1000"
        viewModel = ViewModelProviders.of(this.getActivity()).get(ViewModel::class.java)

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
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    @Override
    fun onViewCreated(@NonNull view: View, @Nullable savedInstanceState: Bundle) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)
        song = Song()
        adapter = NotesAdapter(clickableNotes)
        noteRecycler!!.setAdapter(adapter)
        noteRecycler!!.setLayoutManager(LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false))
        val noteRes = Arrays.asList(getResources().getStringArray(R.array.spinner_array))
        for (i in 0 until noteRes.size()) {
            clickableNotes.add(ClickableNote(
                    noteRes.get(i),
                    getActivity()
                            .getResources()
                            .getIdentifier(
                                    noteRes.get(i).toLowerCase(),
                                    "raw",
                                    getActivity().getPackageName()
                            ),
                    getActivity()
                            .getResources()
                            .getIdentifier(
                                    "alto_" + noteRes.get(i).toLowerCase(),
                                    "drawable",
                                    getContext().getPackageName())

            ))
        }
        viewModel!!.setCurrentNote(clickableNotes.get(0))
        adapter!!.setNotesList(clickableNotes)
    }

    private fun deleteNotes() {
        if (song!!.getSongNotes().size() > 0) {
            song!!.deleteNote()
        }

        var onDeleteCurrentNotesDisplay = ""
        for (n in song!!.getSongNotes()) {
            if (currentNotes!!.getText().toString().isEmpty()) {
                onDeleteCurrentNotesDisplay = n.getNote() + " "
            } else {
                onDeleteCurrentNotesDisplay += n.getNote() + " "
            }
        }
        currentNotes!!.setText(onDeleteCurrentNotesDisplay)
    }

    private fun addNotes() {
        val currentNote = viewModel!!.getCurrentNote()
        Log.d("danny", viewModel!!.getCurrentNote().toString())
        rawId = currentNote.getRawNote()
        noteName = currentNote.getNote()

        var durationI = defaultDuration

        if (durationInput!!.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.no_duration_entered_message), Toast.LENGTH_SHORT).show()
        } else {
            durationI = durationInput!!.getText().toString()
        }
        song!!.addNote(Note(
                rawId,
                syllable!!.getText().toString(),
                Integer.parseInt(durationI),
                noteName)
        )
        currentNotes!!.append(String.format("%s ", noteName))
    }

    private fun addSong() {
        if (songTitle!!.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.no_title_entered_message), Toast.LENGTH_SHORT).show()
        } else if (!viewModel!!.getSong(songTitle!!.getText().toString()).getSongTitle().equals("")) {
            Toast.makeText(getActivity(), getString(R.string.title_exists_message), Toast.LENGTH_SHORT).show()
        } else {
            song!!.setSongTitle(songTitle!!.getText().toString())
            viewModel!!.addSong(song)
            Toast.makeText(getActivity(), getString(R.string.song_added_message), Toast.LENGTH_SHORT).show()
        }
    }

    @OnClick(R.id.composeFragment_addSong_button, R.id.composeFragment_deleteNote_button, R.id.composeFragment_library_button, R.id.composeFragment_addNote_button)
    @Override
    fun onClick(v: View) {
        when (v.getId()) {
            R.id.composeFragment_addSong_button -> addSong()
            R.id.composeFragment_addNote_button -> addNotes()
            R.id.composeFragment_deleteNote_button -> deleteNotes()
            R.id.composeFragment_library_button -> listener!!.displayLibrary()
        }
    }

    @Override
    fun onDestroyView() {
        super.onDestroyView()
        clickableNotes.clear()
    }

    companion object {

        fun newInstance(): ComposeFragment {
            return ComposeFragment()
        }
    }
}
