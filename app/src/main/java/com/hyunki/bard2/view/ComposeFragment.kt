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

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

class ComposeFragment : Fragment(), View.OnClickListener {
    private var listener: FragmentInteractionListener? = null
    private var viewModel: ViewModel? = null
    private var song: Song? = null
    private var rawId: Int? = 0
    private var noteName: String? = null
    private var defaultDuration: String? = null
    private var adapter: NotesAdapter? = null
    private val clickableNotes = ArrayList<ClickableNote>()

    @BindView(R.id.composeFragment_songTitle_editText)
    internal var songTitle: EditText? = null
    @BindView(R.id.composeFragment_syllable_editText)
    internal var syllable: EditText? = null
    @BindView(R.id.composeFragment_duration_editText)
    internal var durationInput: EditText? = null
    @BindView(R.id.composeFragment_displayCurrentNotes_textView)
    internal var currentNotes: TextView? = null
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defaultDuration = "1000"
        viewModel = ViewModelProviders.of(this.activity!!).get(ViewModel::class.java)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentInteractionListener) {
            listener = context
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)
        song = Song()
        adapter = NotesAdapter(clickableNotes)
        noteRecycler!!.adapter = adapter
        noteRecycler!!.layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        val noteRes = resources.getStringArray(R.array.spinner_array)
        for (i in 0 until noteRes.size) {
            clickableNotes.add(ClickableNote(
                    noteRes[i],
                    activity?.resources?.getIdentifier(
                            noteRes[i].toLowerCase(),
                            "raw",
                            activity!!.packageName
                    ),
                    activity?.resources?.getIdentifier(
                            "alto_" + noteRes.get(i).toLowerCase(),
                            "drawable",
                            context?.packageName)

            ))
        }
        viewModel!!.currentNote = clickableNotes[0]
        adapter!!.setNotesList(clickableNotes)
    }

    private fun deleteNotes() {
        if (song!!.songNotes.isNotEmpty()) {
            song!!.deleteNote()
        }

        var onDeleteCurrentNotesDisplay = ""
        for (n in song!!.songNotes) {
            if (currentNotes!!.text.toString().isEmpty()) {
                onDeleteCurrentNotesDisplay = n.note + " "
            } else {
                onDeleteCurrentNotesDisplay += n.note + " "
            }
        }
        currentNotes!!.text = onDeleteCurrentNotesDisplay
    }

    private fun addNotes() {
        val currentNote = viewModel!!.currentNote
        Log.d("danny", viewModel!!.currentNote.toString())

        rawId = currentNote?.rawNote
        noteName = currentNote?.note

        var durationI = defaultDuration

        if (durationInput!!.text.toString().isEmpty()) {
            Toast.makeText(activity, getString(R.string.no_duration_entered_message), Toast.LENGTH_SHORT).show()
        } else {
            durationI = durationInput!!.getText().toString()
        }
        song!!.addNote(Note(
                rawId,
                syllable!!.text.toString(),
                Integer.parseInt(durationI),
                noteName)
        )
        currentNotes!!.append(String.format("%s ", noteName))
    }

    private fun addSong() {
        if (songTitle!!.text.toString().isEmpty()) {
            Toast.makeText(activity, getString(R.string.no_title_entered_message), Toast.LENGTH_SHORT).show()
        } else if (viewModel!!.getSong(songTitle!!.text.toString()).songTitle != "") {
            Toast.makeText(activity, getString(R.string.title_exists_message), Toast.LENGTH_SHORT).show()
        } else {
            song!!.songTitle = songTitle!!.text.toString()
            viewModel!!.addSong(song!!)
            Toast.makeText(activity, getString(R.string.song_added_message), Toast.LENGTH_SHORT).show()
        }
    }

    @OnClick(R.id.composeFragment_addSong_button, R.id.composeFragment_deleteNote_button, R.id.composeFragment_library_button, R.id.composeFragment_addNote_button)
    override fun onClick(v: View) {
        when (v.id) {
            R.id.composeFragment_addSong_button -> addSong()
            R.id.composeFragment_addNote_button -> addNotes()
            R.id.composeFragment_deleteNote_button -> deleteNotes()
            R.id.composeFragment_library_button -> listener!!.displayLibrary()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clickableNotes.clear()
    }

    companion object {

        fun newInstance(): ComposeFragment {
            return ComposeFragment()
        }
    }
}
