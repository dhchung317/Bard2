package com.hyunki.bard2.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.hyunki.bard2.databinding.FragmentComposeBinding
import java.util.*
import kotlin.collections.ArrayList

class ComposeFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentComposeBinding
    private lateinit var listener: FragmentInteractionListener
    private lateinit var viewModel: ViewModel
    private lateinit var notes: MutableList<Note>
    private var rawId: Int = 0
    private lateinit var noteName: String
    private val defaultDuration = "1000"
    private lateinit var adapter: NotesAdapter
    private val clickableNotes = ArrayList<ClickableNote>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this.activity!!).get(ViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentInteractionListener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentComposeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notes = ArrayList()
        adapter = NotesAdapter(clickableNotes)
        binding.composeRecyclerview.adapter = adapter
        binding.composeRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        setClickableNotes(resources.getStringArray(R.array.spinner_array))
        adapter.setNotesList(clickableNotes)
        viewModel.currentNote = clickableNotes[0]
        setListeners()
    }

    private fun setClickableNotes(noteRes: Array<String>) {
        for (i in noteRes.indices) {
            clickableNotes.add(ClickableNote(
                    note = noteRes[i],
                    rawNote = activity?.resources?.getIdentifier(
                            noteRes[i].toLowerCase(Locale.getDefault()),
                            "raw",
                            activity!!.packageName
                    )!!,
                    imgSrc = activity?.resources?.getIdentifier(
                            "alto_" + noteRes[i].toLowerCase(Locale.getDefault()),
                            "drawable",
                            context?.packageName)!!
            ))
        }
    }

    private fun setListeners() {
        binding.composeFragmentAddSongButton.setOnClickListener(this)
        binding.composeFragmentAddNoteButton.setOnClickListener(this)
        binding.composeFragmentDeleteNoteButton.setOnClickListener(this)
        binding.composeFragmentLibraryButton.setOnClickListener(this)
    }

    private fun deleteNotes() {
        if (notes.isNotEmpty()) {
            notes.remove(notes.last())
        }
        refreshCurrentNotesDisplay(notes, binding.composeFragmentDisplayCurrentNotesTextView)
    }

    private fun refreshCurrentNotesDisplay(notes: List<Note>, currentNotesTextView: TextView) {
        var newText = ""
        for (n in notes) {
            if (currentNotesTextView.text.isEmpty()) {
                newText = "${n.note} "
            } else {
                newText += "${n.note} "
            }
        }
        currentNotesTextView.text = newText
    }

    private fun addNotes() {
        val currentNote = viewModel.currentNote
        Log.d("danny", viewModel.currentNote.toString())
        rawId = currentNote.rawNote
        noteName = currentNote.note
        val durationI = checkDuration(binding.composeFragmentDurationEditText.text.toString())
        notes.add(Note(
                rawId,
                binding.composeFragmentSyllableEditText.text.toString(),
                Integer.parseInt(durationI),
                noteName)
        )
        binding.composeFragmentDisplayCurrentNotesTextView.append(String.format("%s ", noteName))
    }

    private fun checkDuration(d: String): String {
        return if (d.isEmpty()) {
            defaultDuration
        } else {
            d
        }
    }

    private fun addSong(title: String, notes: MutableList<Note>) {
        when {
            title.isEmpty() -> Toast.makeText(activity, getString(R.string.no_title_entered_message), Toast.LENGTH_SHORT).show()
            viewModel.getRepository().getSong(title).songTitle == title -> {
                Toast.makeText(activity, getString(R.string.title_exists_message), Toast.LENGTH_SHORT).show()
            }
            else -> {
                viewModel.addSong(Song(title, notes))
                Toast.makeText(activity, getString(R.string.song_added_message), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.composeFragment_addSong_button -> addSong(binding.composeFragmentSongTitleEditText.text.toString(), notes)
            R.id.composeFragment_addNote_button -> addNotes()
            R.id.composeFragment_deleteNote_button -> deleteNotes()
            R.id.composeFragment_library_button -> listener.displayLibrary()
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
